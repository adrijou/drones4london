package com.propero.drones;

import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileException;
import com.propero.drones.pojo.Coordinates;
import com.propero.drones.pojo.DronOrder;
import com.propero.drones.pojo.TrafficReport;
import com.propero.drones.pojo.TubeStation;
import com.propero.drones.utils.CsvTubeStations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public final class Dron extends Thread {


    private static final Logger LOG
            = LoggerFactory.getLogger(Dron.class);
    private static final int RADIO = 350;
    private static final double LATITUDE_INIT = 51.508066;
    private static final double LONGITUDE_INIT = -0.163169;


    private int pid;
    private OrdersQueue dronOrdersQueue = new OrdersQueue();
    private static List<TubeStation> tubeStationList;
    private TubeStation tubeStation = new TubeStation();
    private static final String CSV_FILE_TUBE = "tube.csv";
    private Coordinates currentPoint = new Coordinates();
    private TrafficReport trafficReport;
    private Dispatcher serverDispatcher;

    static {
        try {
            tubeStationList =
                    CsvTubeStations.newInstance()
                            .getTubeStationFromCSV(CSV_FILE_TUBE);
        } catch (NonCSVFileFoundException e) {
            LOG.info("NonCSVFileFoundException. " + e.getMessage());
        } catch (UnsupportedCSVFileException e) {
            LOG.debug("UnsupportedCSVFileException. " + e.getMessage());
        }

    }

    public Dron(final int pid) {
        this.pid = pid;
        //Assuming that drones will always start from Hyde Park.
        currentPoint.setLatitude(LATITUDE_INIT);
        currentPoint.setLongitude(LONGITUDE_INIT);
    }

    /**
     * Until interrupted, reads messages from the client socket, forwards them
     * to the server dispatcher's queue and notifies the server dispatcher.
     */
    public void run() {
        DronOrder dronOrder;
        try {
            while (!isInterrupted()) {

                dronOrder = (DronOrder) dronOrdersQueue.pull();

                moveToNextCoordinate(dronOrder);

                tubeStation = findTubeStationInArea((dronOrder));

                if (tubeStation != null) {
                    trafficReport = new TrafficReport(
                            dronOrder.getPid(), dronOrder.getTime());
                    serverDispatcher
                            .printTrafficReportFromDron(trafficReport);
                }
            }

        } catch (NonCSVFileFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedCSVFileException e) {
            e.printStackTrace();
        }
        // Communication is broken. Interrupt the thread and delete the dron
        // from the list of drones of the Dispatcher server

        interrupt();
        serverDispatcher.deleteDron(pid);
    }

    private TubeStation findTubeStationInArea(final DronOrder coordinates)
            throws NonCSVFileFoundException, UnsupportedCSVFileException {

        LOG.debug("Tube station list has " + tubeStationList.size()
                + " elements");

        for (TubeStation tubeStationVar : tubeStationList) {
            if (isInsideArea(tubeStationVar, coordinates)) {
                LOG.debug("Got tube station " + tubeStationVar.getTubeName()
                        + "in the given area");
                return tubeStationVar;
            }
        }
        LOG.debug("No tube station inside of the given area");
        return null;
    }

    private boolean isInsideArea(final TubeStation tubeStation,
                                 final DronOrder coordinates) {

        Double xCentre = coordinates.getLongitude()
                , yCentre = coordinates.getLatitude()
                , xPoint = tubeStation.getLongitude()
                , yPoint = tubeStation.getLatitude();

        return (Math.sqrt((xCentre - xPoint) + (yCentre - yPoint)) <= RADIO);

    }


    private void moveToNextCoordinate(final DronOrder nextCoordinates) {
        currentPoint.setLatitude(currentPoint.getLatitude()
                                + nextCoordinates.getLatitude());
        currentPoint.setLongitude(currentPoint.getLongitude()
                              + nextCoordinates.getLongitude());

    }


    public int getPid() {
        return pid;
    }


    public OrdersQueue getDronOrdersQueue() {
        return dronOrdersQueue;
    }



}
