package com.propero.drones.utils;

import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileExceptionImpl;
import com.propero.drones.pojo.DronOrder;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public class CsvDronOrdersTest {

    @Test
    public void readingTestResourceFile() throws IOException {

        String csvXMLFile = getFile("locatedFile.txt");
        assertNotNull(csvXMLFile);
    }

    @Test
    public void coordinatesDronListFromCSVFile()
            throws NonCSVFileFoundException, UnsupportedCSVFileExceptionImpl {

        int pid = 123;

        List<DronOrder> listCoordinatesTest
                = CsvDronOrders.newInstance().getDronCoordinatesFromCSV(pid);

        assertEquals(2, listCoordinatesTest.size());
        assertEquals(123, listCoordinatesTest.get(0).getPid());
        assertEquals(123, listCoordinatesTest.get(1).getPid());
        assertEquals("51.476105",listCoordinatesTest.get(0).getLongitude());
        assertEquals("-0.100224", listCoordinatesTest.get(0).getLatitude());
        assertEquals("2011-03-22 07:55:26", listCoordinatesTest.get(0).getTime());
    }


    @Test(expected = NonCSVFileFoundException.class)
    public void dronOrdersListCSVFileNotGiven()
            throws UnsupportedCSVFileExceptionImpl, NonCSVFileFoundException {
        int pid = 100;

        List<DronOrder> listDronOrders
                = CsvDronOrders.newInstance().getDronCoordinatesFromCSV(pid);
    }

    @Test(expected = UnsupportedCSVFileExceptionImpl.class)
    public void dronOrderListCSVContainWrongParameters()
            throws UnsupportedCSVFileExceptionImpl, NonCSVFileFoundException {
        int pid = 666;

        List<DronOrder> listDronOrders
                = CsvDronOrders.newInstance().getDronCoordinatesFromCSV(pid);
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
