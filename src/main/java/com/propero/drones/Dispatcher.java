package com.propero.drones;

import com.propero.drones.exceptions.DuplicateDronPIDException;
import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileException;
import com.propero.drones.pojo.DronOrder;
import com.propero.drones.pojo.TrafficReport;
import com.propero.drones.utils.CsvDronOrders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Timer;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public final class Dispatcher extends Thread {

    private static final Logger LOG
            = LoggerFactory.getLogger(Dispatcher.class);

    private Map dronCsvOrderList;

    private Map dronesList = new HashMap();
    private SimpleDateFormat sdf;

    public Dispatcher() throws ParseException {
        dronCsvOrderList = new HashMap<Integer, List<DronOrder>>();
        //Starting time
        sdf = new java.text.SimpleDateFormat("HH:mm");
        sdf.parse("07:45");

    }

    public void addDron(final Dron dron)
            throws DuplicateDronPIDException {

        if (dronesList.containsKey(dron.getPid())) {
            LOG.debug("trying to insert an existing pid dron");
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
    public void printTrafficReportFromDron(
                                            final TrafficReport trafficReport) {
        LOG.info("Printing traffic report from dron "
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
    public void sendDronOrders(final int pid,
                                    final List<DronOrder> dronOrderList)
            throws InterruptedException {
        for (DronOrder dronOrder : dronOrderList) {
            ((Dron) dronesList.get(pid)).getDronOrdersQueue()
                            .enqueue(dronOrder);
        }
    }

    /**
     *  Deletes given dron from the dispatcher's dron list if an
     *  exception happens with the dron.
     * @param dronPid
     */
    public void deleteDron(final int dronPid) {
        dronesList.remove(dronPid);
    }

    /**
     * Generate a signal to stop all dron threads by using interrupt method
     */
    public void stopAllDrones() {
        Iterator entries = dronesList.values().iterator();
        while (entries.hasNext()) {
            Dron dron = (Dron) entries.next();
            dron.interrupt();
        }
        dronesList.clear();
    }

    /**
     * Infinitely reads traffic reports from the queue
     * and print them into the logs
     */
   @Override
    public void run() {
   //TODO
       TrafficReport trafficReport;
        SimpleDateFormat sdfEnd = new SimpleDateFormat("HH:mm");
        try {
            sdfEnd.parse("08:10");
        } catch (ParseException e) {
            LOG.debug("Error generating end date");
        }
        //This has been emulated via time = 10*1000
        Timer timer = new Timer();

     /*   while (true) {
            /*List<DronOrder> dronOrderList1 =
                    readCsvDronOrdersFile(dron);

            sendDronOrders(dron.getPid(), dronOrderList1);
        }


        stopAllDrones();*/
    }



 /*   public void closeClients() {
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
