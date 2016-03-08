package com.propero.drones;


import com.propero.drones.constants.Constants;
import com.propero.drones.exceptions.DuplicateDronPIDException;
import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileException;
import com.propero.drones.pojo.DronOrder;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppDrons4LondonTest {

    @Test
    public void testDrones4LondonAppOneDron()
            throws ParseException, DuplicateDronPIDException,
            NonCSVFileFoundException, UnsupportedCSVFileException, InterruptedException {

        Dispatcher serverDispatcher = new Dispatcher();
        serverDispatcher.start();

        List<Dron> dronList = new ArrayList<>();

        Dron dron5937 = new Dron(Constants.DRON_1, serverDispatcher);
        dronList.add(dron5937);
        serverDispatcher.addDron(dron5937);

        //Starting a new thread per each dron
        dron5937.start();
        //Set name of the thread
        dron5937.setName(String.valueOf(dron5937.getPid()));
        //instantiate dron order lists from csv files.
        List<DronOrder> dronOrderList1 =
                serverDispatcher.readCsvDronOrdersFile(dron5937);

        serverDispatcher.sendDronOrders(dron5937.getPid(), dronOrderList1);

    }

    @Test
    public void testDrones4LondonAppTwoDronesConcurrency() {
        //TODO
    }

}
