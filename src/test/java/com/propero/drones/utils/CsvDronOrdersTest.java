package com.propero.drones.utils;

import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileException;
import com.propero.drones.pojo.DronOrder;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public class CsvDronOrdersTest {

    private static final double DELTA = 1e-15;


    @Test
    public void readingTestResourceFile() throws IOException {

        String csvXMLFile = getFile("locatedFile.txt");
        assertNotNull(csvXMLFile);
    }

    @Test
    public void coordinatesDronListFromCSVFile()
            throws NonCSVFileFoundException, UnsupportedCSVFileException {

        int pid = 123;

        List<DronOrder> listCoordinatesTest
                = CsvDronOrders.newInstance().getDronCoordinatesFromCSV(pid);

        assertEquals(2, listCoordinatesTest.size());
        assertEquals(123, listCoordinatesTest.get(0).getPid());
        assertEquals(123, listCoordinatesTest.get(1).getPid());
        assertThat(51.476105,
                closeTo(listCoordinatesTest.get(0).getLatitude(), DELTA));
        assertThat(-0.100224,
                closeTo(listCoordinatesTest.get(0).getLongitude(), DELTA));
        assertEquals(Timestamp.valueOf("2011-03-22 07:55:26").getTime(),
                listCoordinatesTest.get(0).getTime());
    }


    @Test(expected = NonCSVFileFoundException.class)
    public void dronOrdersListCSVFileNotGiven()
            throws UnsupportedCSVFileException, NonCSVFileFoundException {
        int pid = 100;

        List<DronOrder> listDronOrders
                = CsvDronOrders.newInstance().getDronCoordinatesFromCSV(pid);
    }

    @Test(expected = UnsupportedCSVFileException.class)
    public void dronOrderListCSVContainWrongParameters()
            throws UnsupportedCSVFileException, NonCSVFileFoundException {
        int pid = 666;

        List<DronOrder> listDronOrders
                = CsvDronOrders.newInstance().getDronCoordinatesFromCSV(pid);
    }


    @Test(expected = UnsupportedCSVFileException.class)
    public void dronOrdersFileListContainWrongFormatFile()
            throws UnsupportedCSVFileException, NonCSVFileFoundException,
            FileNotFoundException, URISyntaxException {

        int dronOrderFile = 999;
        updateFile(Integer.toString(dronOrderFile)+".csv", "\"Vauxhall\",,,,\"51.484833\"\n");

        List<DronOrder> dronList
                = CsvDronOrders.newInstance()
                .getDronCoordinatesFromCSV(dronOrderFile);
    }

    @Test(expected = UnsupportedCSVFileException.class)
    public void tubeStationsFileListContainWrongFormatFileSeparator()
            throws UnsupportedCSVFileException, NonCSVFileFoundException,
            FileNotFoundException, URISyntaxException {

        int dronOrderFile = 999;
        updateFile(Integer.toString(dronOrderFile)+".csv", "\"Vauxhall\":\"51.484833\"\n");

        List<DronOrder> dronOrderList
                = CsvDronOrders.newInstance()
                .getDronCoordinatesFromCSV(dronOrderFile);
    }

    private String getFile(String fileName){

        String result = "";
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void updateFile(String fileName, String content)
            throws FileNotFoundException {
        try {
            //open file in override mode
            FileOutputStream fop =
                    new FileOutputStream(new File(
                            getClass().getClassLoader()
                                    .getResource(fileName).toURI()));

            // get the content in bytes
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }





}
