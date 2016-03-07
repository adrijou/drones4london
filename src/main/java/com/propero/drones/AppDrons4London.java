package com.propero.drones;

import com.propero.drones.exceptions.DuplicateDronPIDException;
import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileException;
import com.propero.drones.pojo.DronOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public final class AppDrons4London {

    public static final int LISTENING_PORT = 2002;
    private static final Logger LOG
            = LoggerFactory.getLogger(AppDrons4London.class);
    public static final int DRON_1 = 5937;
    public static final int DRON_2 = 6043;


    private AppDrons4London() {
        throw new AssertionError();
    }

    public static void main(final String[] args) {

        Dispatcher serverDispatcher = new Dispatcher();

        List<Dron> dronList = new ArrayList<>();

        Dron dron5937 = new Dron(DRON_1);
        Dron dron6043 = new Dron(DRON_2);

        dronList.add(dron5937);
        dronList.add(dron6043);

        for (Dron dron : dronList) {
            try {
                //Check if that dron is already registered
                serverDispatcher.addDron(dron);
                //Starting a new thread per each dron
                dron.start();
                //Set name of the thread
                dron.setName(String.valueOf(dron.getPid()));
                //instantiate dron order lists from csv files.
                List<DronOrder> dronOrderList1 =
                        serverDispatcher.readCsvDronOrdersFile(dron);

                serverDispatcher.sendDronOrders(dron.getPid(), dronOrderList1);

            } catch (DuplicateDronPIDException e) {
                e.printStackTrace();
            } catch (UnsupportedCSVFileException e) {
                e.printStackTrace();
            } catch (NonCSVFileFoundException e) {
                e.printStackTrace();
            }
        }
    }


}
