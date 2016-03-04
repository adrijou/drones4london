package com.propero.drones.utils;

import au.com.bytecode.opencsv.CSVReader;
import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileExceptionImpl;
import com.propero.drones.pojo.DronOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public final class CsvDronOrders {

    private static final Logger LOG =
            LoggerFactory.getLogger(CsvDronOrders.class);
    private static final int THREE = 3;


    private CsvDronOrders() {
    }

    public static CsvDronOrders newInstance() {
        return new CsvDronOrders();
    }

    public List<DronOrder> getDronCoordinatesFromCSV(final int pid)
            throws NonCSVFileFoundException, UnsupportedCSVFileExceptionImpl {

        LOG.debug("_Accessing to csv of dron order coordinates named ",
                pid + "csv");

        String nameFile;
        CSVReader reader = null;
        List<DronOrder> dronOrdersList = new ArrayList<DronOrder>();
        DronOrder dronOrder;
        nameFile = Integer.toString(pid) + ".csv";

        try {
            //Get the CSVReader instance with specifying the delimiter to be use
            InputStream is = CsvDronOrders.class.getClassLoader()
                    .getResourceAsStream(nameFile);
            if (is == null) {
                throw new NonCSVFileFoundException(nameFile);
            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, "UTF-8"));
            reader = new CSVReader(br);
            String[] nextLine;
            //Read one line at a time
            LOG.debug("csv file read. "
                    + "Iterating over the list of coordinates");
            while ((nextLine = reader.readNext()) != null) {
                dronOrder = new DronOrder(Integer.parseInt(nextLine[0]));
                dronOrder.setLongitude(nextLine[1]);
                dronOrder.setLatitude(nextLine[2]);
                dronOrder.setTime(nextLine[THREE]);

                dronOrdersList.add(dronOrder);
            }
        } catch (IOException e) {
            LOG.info("Error reading file" + e.getMessage());
        } catch (NonCSVFileFoundException ex) {
            throw new NonCSVFileFoundException(nameFile);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new UnsupportedCSVFileExceptionImpl(nameFile);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dronOrdersList;
    }




}
