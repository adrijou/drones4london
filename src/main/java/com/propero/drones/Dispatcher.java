package com.propero.drones;

import com.propero.drones.exceptions.DuplicateDronPIDException;
import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileException;
import com.propero.drones.pojo.DronOrder;
import com.propero.drones.pojo.TrafficReport;
import com.propero.drones.utils.CsvDronOrders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public final class Dispatcher {

    private static final Logger LOG
            = LoggerFactory.getLogger(Dispatcher.class);

    private Map dronCsvOrderList;

    private Map dronesList = new HashMap();

    public Dispatcher() {
        dronCsvOrderList = new HashMap<Integer, List<DronOrder>>();
    }

    public synchronized void addDron(final Dron dron)
            throws DuplicateDronPIDException {

        if (dronesList.containsKey(dron.getPid())) {
          throw new DuplicateDronPIDException(dron.getPid());
        }
        dronesList.put(dron.getPid(), dron);
    }

    public List<DronOrder> readCsvDronOrdersFile(final Dron dron)
            throws NonCSVFileFoundException, UnsupportedCSVFileException {

        List<DronOrder> dronesOrdersList = new ArrayList<>();

        dronesOrdersList.addAll(
                CsvDronOrders.newInstance().getDronCoordinatesFromCSV(
                            dron.getPid()));

        return dronesOrdersList;
    }

    /**
     * @return and deletes the next trafficReport from the message queue.
     * If there is no reports in the queue,
     * falls in sleep until notified by dispatchMessage method.
     */
    public synchronized void printTrafficReportFromDron(
                                            final TrafficReport trafficReport) {
        LOG.debug("Printing traffic report from dron "
                + trafficReport.getDronID());
        LOG.info("Traffic report: " + trafficReport.toString());
    }

    /**
     * Uses per each dron defined by pid, the list of orders to be sent. As
     * dron.ordersQueue is a limited queue (10), will need to wait till there's
     * availability on memory for storing next dronOrder.
     * @param pid
     * @param dronOrderList
     */
    public synchronized void sendDronOrders(final int pid,
                                    final List<DronOrder> dronOrderList) {
        for (DronOrder dronOrder : dronOrderList) {
            ((Dron) dronesList.get(pid)).getDronOrdersQueue().put(dronOrder);
        }
    }

    /**
     *  Deletes given dron from the dispatcher's dron list if an
     *  exception happens with the dron.
     * @param dronPid
     */
    public synchronized void deleteDron(final int dronPid) {
        dronesList.remove(dronPid);
    }

    /**
     * Generate a signal to stop all dron threads
     */
    public synchronized void stopAllDrones() {
        Iterator entries = dronesList.values().iterator();
        while (entries.hasNext()) {
            Dron dron = (Dron) entries.next();
            dron.interrupt();
        }
        dronesList.clear();
    }


  /*  public synchronized void addOrderList2Dron(final int pid,
                                     final List<DronOrder> dronOrderList) {
        dronCsvOrderList.put(pid, dronOrderList);
    }
*/
    /**
     * Infinitely reads traffic reports from the queue
     * and print them into the logs
     */
   /*  @Override
    public void run()
    {
       TrafficReport trafficReport;
        try {
            //TODO check condition for stopping at 8:10
            while (true) {
                for ()
            }
        } catch (DuplicateDronPIDException e) {
            LOG.info("Tried to add a dron already running. " + e.getMessage());
        } catch (NonCSVFileFoundException e) {
            LOG.info("NonCSVFileFoundException." + e.getMessage());
        } catch (UnsupportedCSVFileException e) {
            LOG.info("UnsupportedCSVFileException. " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            stopAllDrones();

        }
    }*/


/*
    public void closeClients() {
        long in1minute = 10*1000;
        Timer timer = new Timer();
        timer.schedule( new TimerTask(){
            public void run() {
                for(int i=0;i<startedThreads.size();i++){
                    try {
                        startedThreads.get(i).close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                listening=false;
            }
        },  in1minute );

    }

    public void close() throws IOException{
        String outputLine = p.processInput("shut");
        out.println(outputLine);
        out.close();
        in.close();
        socket.close();
    }
    */

     public Map getDronesList() {
         return dronesList;
     }

    public void setDronesList(final Map dronesList) {
        this.dronesList = dronesList;
    }

    public Map getDronCsvOrderList() {
        return dronCsvOrderList;
    }

    public void setDronCsvOrderList(final Map dronCsvOrderList) {
        this.dronCsvOrderList = dronCsvOrderList;
    }


}
