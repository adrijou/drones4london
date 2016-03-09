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
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public final class Dispatcher {

    private static final Logger LOG
            = LoggerFactory.getLogger(Dispatcher.class);

    private Map dronCsvOrderList;

    private Map<Integer, List<DronOrder>> ordersInList
            = new HashMap<Integer, List<DronOrder>>();

    private Map dronesList = new HashMap();

    public Dispatcher() throws ParseException {
        dronCsvOrderList = new HashMap<Integer, List<DronOrder>>();
    }

    public void addDron(final Dron dron)
            throws DuplicateDronPIDException {

        if (dronesList.containsKey(dron.getPid())) {
            LOG.debug("trying to insert an existing pid dron");
          throw new DuplicateDronPIDException(dron.getPid());
        }
        dronesList.put(dron.getPid(), dron);
    }

    /**
     *
     * @param dronPid
     * @throws NonCSVFileFoundException
     * @throws UnsupportedCSVFileException
     */
    public void initiliceListOrders(final int dronPid)
            throws NonCSVFileFoundException, UnsupportedCSVFileException {

        List<DronOrder> dronOrderList1 =
                readCsvDronOrdersFile(dronPid);
        ordersInList.put(dronPid, (List<DronOrder>) dronOrderList1);
    }

    public List<DronOrder> readCsvDronOrdersFile(final int dronPid)
            throws NonCSVFileFoundException, UnsupportedCSVFileException {

        List<DronOrder> dronesOrdersList = new ArrayList<>();

        dronesOrdersList.addAll(
                CsvDronOrders.newInstance().getDronCoordinatesFromCSV(dronPid));

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

    public Map<Integer, List<DronOrder>> getOrdersInList() {
        return ordersInList;
    }

    public void setOrdersInList(final Map<Integer, List<DronOrder>>
                                         ordersInList) {
        this.ordersInList = ordersInList;
    }

}
