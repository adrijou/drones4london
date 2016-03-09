package com.propero.drones;

import com.propero.drones.constants.Constants;
import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileException;
import com.propero.drones.pojo.DronOrder;
import com.propero.drones.pojo.TrafficReport;
import com.propero.drones.pojo.TubeStation;
import com.propero.drones.utils.BlockingQueue;
import com.propero.drones.utils.CsvTubeStations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public final class Dron extends Thread {


    private static final Logger LOG
            = LoggerFactory.getLogger(Dron.class);

    // Must be volatile: will never be cached thread-locally:
    // all reads and writes will go straight to "main memory";
    // Access to the variable is in a synchronized block,synchronized on itself.
    private volatile boolean shutdown = false;

    private int pid;
    private BlockingQueue dronOrdersQueue =
            new BlockingQueue(Constants.MEM_SIZE);
    private static List<TubeStation> tubeStationList;
    private static final String CSV_FILE_TUBE = "tube.csv";
    private DronOrder currentPoint;
    private Dispatcher serverDispatcher;

    static {
        try {
            tubeStationList =
                    CsvTubeStations.newInstance()
                            .getTubeStationFromCSV(CSV_FILE_TUBE);
        } catch (NonCSVFileFoundException e) {
            LOG.debug("NonCSVFileFoundException. " + e.getMessage());
        } catch (UnsupportedCSVFileException e) {
            LOG.debug("UnsupportedCSVFileException. " + e.getMessage());
        }
    }

    public static final Date TIME_TO_START;
    //We will only create one instance of the starting time
    static {
        Calendar calendar = Calendar.getInstance();
        //The app will start running at 7:45am.
        calendar.set(Calendar.HOUR_OF_DAY, Constants.SEVEN);
        calendar.set(Calendar.MINUTE, Constants.FORTY_FIVE);
        calendar.set(Calendar.SECOND, 0);
        TIME_TO_START = calendar.getTime();
    }

    /**
     * @param pid
     * @param dispatcher
     * @throws ParseException
     */
    public Dron(final int pid, final Dispatcher dispatcher)
            throws ParseException {
        this.pid = pid;
        this.serverDispatcher = dispatcher;
        this.currentPoint = new DronOrder(pid);
        currentPoint.setLatitude(Constants.LATITUDE_INIT);
        currentPoint.setLongitude(Constants.LONGITUDE_INIT);
        //initialize time at the moment when the app runs.
        //Assuming that this will be always at the same time (7:45am)
        long timeStart = Timestamp.valueOf(Constants.TIME_START_WORKING)
                                                            .getTime();
        currentPoint.setTime(timeStart);
    }


    /**
     * //TODO this can be considered as an extension of the program
     * @param pid
     * @param dispatcher
     * @param timeStart: custom start working time for a Dron
     * @throws ParseException
     */
    public Dron(final int pid, final Dispatcher dispatcher,
                final long timeStart) throws ParseException {
        this.pid = pid;
        this.serverDispatcher = dispatcher;
        this.currentPoint = new DronOrder(pid);
        currentPoint.setLatitude(Constants.LATITUDE_INIT);
        currentPoint.setLongitude(Constants.LONGITUDE_INIT);
        currentPoint.setTime(timeStart);
    }

    /**
     * Until interrupted, reads messages from the client socket, forwards them
     * to the server dispatcher's queue and notifies the server dispatcher.
     */
    public void run() {
        DronOrder dronOrder;
        Double distance, speed;
        TubeStation tubeStation;
        TrafficReport trafficReport;
        int orderCounter = 0;
        try {
            while (!isInterrupted() && !shutdown) {
                LOG.debug("Getting the next order from the queue to be "
                        + "handled by " + Thread.currentThread().getName());
                if (serverDispatcher.getOrdersInList().get(pid) != null
                  && serverDispatcher.getOrdersInList()
                                    .get(pid).size() != 0) {

                    dronOrdersQueue.enqueue(
                            serverDispatcher.getOrdersInList().get(pid).get(0));
                    //Deleting order processed
                    serverDispatcher.getOrdersInList().get(pid).remove(0);

                    LOG.info("Processing order number: " + (orderCounter++)
                            + " for Dron: " + pid);

                    dronOrder = (DronOrder) dronOrdersQueue.dequeue();

                    LOG.debug("Dron order pid got as " + dronOrder.getPid());
                    distance = moveToNextCoordinate(dronOrder);

                    tubeStation = findTubeStationInArea((dronOrder));
                    if (tubeStation != null) {
                        LOG.debug("Found tube station: "
                                + tubeStation.getTubeName());
                        speed = distance
                                / Math.abs(currentPoint.getTime()
                                - dronOrder.getTime());

                        trafficReport = new TrafficReport(
                                dronOrder.getPid(), tubeStation.getTubeName(),
                                dronOrder.getTime(), speed);
                        LOG.debug("Sending to dispatcher the traffic report"
                                + " for dron " + trafficReport.getDronID());
                        serverDispatcher
                                .printTrafficReportFromDron(trafficReport);
                    }
                }
            }

            if (shutdown) {
                LOG.info("Detected SHUTDOWN signal. Stopping dron: " + pid);
            }

        } catch (NonCSVFileFoundException e) {
            LOG.debug("NonCSVFileFoundException" + e);
        } catch (UnsupportedCSVFileException e) {
            LOG.debug("UnsupportedCSVFileException" + e);
        } catch (InterruptedException e) {
            LOG.debug("InterruptedException" + e);
        }
        // Communication is broken. Interrupt the thread and delete the dron
        // from the list of drones of the Dispatcher server

        interrupt();
        serverDispatcher.deleteDron(pid);
    }

    public void requestStop() {
        shutdown = true;
    }

    public TubeStation findTubeStationInArea(final DronOrder coordinates)
            throws NonCSVFileFoundException, UnsupportedCSVFileException {

        LOG.debug("Tube station list has " + tubeStationList.size()
                + " elements");

        for (TubeStation tubeStationVar : tubeStationList) {
            if (isInsideArea(
                    tubeStationVar.getLatitude(), tubeStationVar.getLongitude(),
                    coordinates.getLatitude(), coordinates.getLongitude())) {

                LOG.debug("Got tube station " + tubeStationVar.getTubeName()
                        + " in the given area");
                return tubeStationVar;
            }
        }
        LOG.debug("No tube station inside of the given area");
        return null;
    }

    public double moveToNextCoordinate(final DronOrder nextCoordinates)
            throws InterruptedException {

        double lat1 = currentPoint.getLatitude();
        double long1 = currentPoint.getLongitude();
        double lat2 = nextCoordinates.getLatitude();
        double long2 = nextCoordinates.getLongitude();

        double distanceToMove = distance(lat1, long1, lat2, long2);

        // virtual waiting time between both points in fast forward 100K times
        //TODO include at the end to avoid waiting times.
        LOG.info("Time next station is: "
                + new Timestamp(nextCoordinates.getTime()));
        LOG.info("Sleep the process as a virtual scenario of time "
                + "spend moving to the next point: "
                + (nextCoordinates.getTime() - currentPoint.getTime())
                                                        / Constants.HUNDRED_K
                + "milliseconds");
        sleep((nextCoordinates.getTime() - currentPoint.getTime())
                                                        / Constants.HUNDRED_K);

        //Update new coordinates point
        currentPoint.setLatitude(nextCoordinates.getLatitude());
        currentPoint.setLongitude(nextCoordinates.getLongitude());
        LOG.debug("New coordinates point is: (" + currentPoint.getLatitude()
                + " , " + currentPoint.getLongitude() + ")");

        return distanceToMove;
    }

    /*
     * Calculate distance between two points in latitude and longitude
     * lat1, lon1 Start point lat2, lon2 En
     * @returns Distance
     * this could be got by using Math library such as:
     * distance = Math.hypot(long2 - long1, lat2 - lat1);
     */
    public double distance(final double lat1, final double lon1,
                            final double lat2, final double lon2) {
       //Returned in kms
       return Math.sqrt(
               (lon2 - lon1) * (lon2 - lon1) + (lat2 - lat1) * (lat2 - lat1))
               * Constants.CONVERT_TO_KMS;
    }

    /**
     *
     * @param distance
     * @param time
     * @return
     */
    private double velocity(final double distance, final double time) {
        return distance / time;
    }

    /*
     * Calculate if a given point (lat1, lon1) is inside of a circle given by
     * Radio=350mtrs and centre point(lat2, lon2)
     *
     * @returns Distance given by Pythagoras theorem
     */
    private boolean isInsideArea(final double lat1, final double lon1,
                                final double lat2, final double lon2) {

        return distance(lat1, lon1, lat2, lon2) <= Constants.RADIO_IN_KMS;

    }


    public int getPid() {
        return pid;
    }

    public Dispatcher getServerDispatcher() {
        return serverDispatcher;
    }

    public void setServerDispatcher(final Dispatcher serverDispatcher) {
        this.serverDispatcher = serverDispatcher;
    }

    public DronOrder getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(final DronOrder currentPoint) {
        this.currentPoint = currentPoint;
    }

    public BlockingQueue getDronOrdersQueue() {
        return dronOrdersQueue;
    }




}
