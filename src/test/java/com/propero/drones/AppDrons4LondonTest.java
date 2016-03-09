package com.propero.drones;


import com.propero.drones.constants.Constants;
import com.propero.drones.exceptions.DuplicateDronPIDException;
import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileException;
import com.propero.drones.pojo.DronOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.assertEquals;


/**
 * Unit test for simple App.
 */
public class AppDrons4LondonTest {

    @Test
    public void testDrones4LondonAppOneDronCheckTimerTask()
            throws ParseException, DuplicateDronPIDException,
            NonCSVFileFoundException, UnsupportedCSVFileException, InterruptedException {

        final Dispatcher serverDispatcher = new Dispatcher();

        final List<Dron>  dronList = new ArrayList<>();

        final Dron dron5937 = new Dron(Constants.DRON_1, serverDispatcher);
        dronList.add(dron5937);
        serverDispatcher.addDron(dron5937);

        //Starting a new thread
        dron5937.start();
        //Set name of the thread
        dron5937.setName(String.valueOf(dron5937.getPid()));

        new Timer(true).schedule(new TimerTask() {
            public void run() {
                for (Dron dron : dronList) {
                    dron.requestStop();
                }
                assertEquals("TERMINATED", dron5937.getState());
            }
        }, 1000);



    }


    @Test
    public void testDrones4LondonAppTwoDronesConcurrency()
            throws ParseException, DuplicateDronPIDException,
            InterruptedException, NonCSVFileFoundException, UnsupportedCSVFileException {
        final Dispatcher serverDispatcher = new Dispatcher();

        final List<Dron>  dronList = new ArrayList<>();

        final Dron dron5937 = new Dron(Constants.DRON_1, serverDispatcher);
        dronList.add(dron5937);
        serverDispatcher.addDron(dron5937);

        final Dron dron6043 = new Dron(Constants.DRON_2, serverDispatcher);
        dronList.add(dron6043);
        serverDispatcher.addDron(dron6043);

        //Set two orders one per each dron
        List<DronOrder> dronOrders = new ArrayList<>();
        dronOrders.add(new DronOrder(Constants.DRON_1));
        serverDispatcher.initiliceListOrders(Constants.DRON_1);

        serverDispatcher.getOrdersInList().put(Constants.DRON_1, dronOrders);

        dronOrders = new ArrayList<>();
        dronOrders.add(new DronOrder(Constants.DRON_2));
        serverDispatcher.getOrdersInList().put(Constants.DRON_2, dronOrders);

        //Set name of the thread
        dron5937.setName(String.valueOf(dron5937.getPid()));
        dron6043.setName(String.valueOf(dron6043.getPid()));

        //Starting a new thread per each dron
        dron5937.start();
        dron6043.start();

        new Timer(true).schedule(new TimerTask() {
            public void run() {
                for (Dron dron : dronList) {
                    dron.requestStop();
                }
                assertEquals("TERMINATED", dron5937.getState());
            }
        }, 10000);

    }

}
