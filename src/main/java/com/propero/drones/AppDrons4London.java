package com.propero.drones;

import com.propero.drones.constants.Constants;
import com.propero.drones.exceptions.DuplicateDronPIDException;
import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Hello world!
 *
 */
public final class AppDrons4London {

    private static final Logger LOG
            = LoggerFactory.getLogger(AppDrons4London.class);


    private AppDrons4London() {
        throw new AssertionError();
    }

    public static void main(final String[] args)
            throws NonCSVFileFoundException, UnsupportedCSVFileException,
            InterruptedException, ParseException {

        LOG.info("Entering application");
        final Dispatcher serverDispatcher;


        try {
            serverDispatcher = new Dispatcher();

            final List<Dron> dronList = new ArrayList<>();

            Dron dron5937 = new Dron(Constants.DRON_1, serverDispatcher);
            Dron dron6043 = new Dron(Constants.DRON_2, serverDispatcher);

            dronList.add(dron5937);
            dronList.add(dron6043);

            for (Dron dron : dronList) {

                //Load the orders in queues per each dron
                serverDispatcher.initiliceListOrders(dron.getPid());
                //Check if that dron is already registered. If not so, add it
                serverDispatcher.addDron(dron);
            }

            for (Dron dron : dronList) {
                //Set name of the thread
                dron.setName(String.valueOf(dron.getPid()));
                //Starting a new thread per each dron
                dron.start();
            }

            //Stop signal at 8:10
            new Timer(true).schedule(new TimerTask() {
                public void run() {
                    LOG.info("Requesting stop");
                    for (Dron dron : dronList) {
                        dron.requestStop();
                    }
                }                           //Fast Forward 240 times
            }, (Constants.STOP_AT_8_10) / Constants.TWO_HUNDRED_FORTY);

            // ((Timestamp.valueOf(Constants.TIME_START_WORKING).getTime() ??

        } catch (DuplicateDronPIDException e) {
            LOG.debug("DuplicateDronPIDException" + e);
        } catch (ParseException e) {
            LOG.debug("ParseException." + e);
        }
    }
}
