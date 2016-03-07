package com.propero.drones;

import com.propero.drones.exceptions.DuplicateDronPIDException;
import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileException;
import com.propero.drones.pojo.DronOrder;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public class DispatcherTest {

    private static final double DELTA = 1e-15;

    @Test
    public void dispatcherClassExists() {
        Dispatcher dispatcher = new Dispatcher();
        assertNotNull(dispatcher);
    }


    @Test
    public void dispatcherHasListDrones() throws DuplicateDronPIDException {
        Dispatcher dispatcher = new Dispatcher();
        int pid1 = 123;
        int pid2= 321;

        Dron dronA = new Dron(pid1);
        Dron dronB = new Dron(pid2);

        dispatcher.addDron(dronA);
        dispatcher.addDron(dronB);

        dronA.setName("123");
        dronB.setName("321");

        assertEquals(2, dispatcher.getDronesList().size());
        assertTrue(dispatcher.getDronesList().get(123) instanceof Thread);
        TestCase.assertEquals("123", ((Thread) dispatcher.getDronesList().get(123)).getName());
        assertTrue(dispatcher.getDronesList().get(321) instanceof Thread);
        TestCase.assertEquals("321", ((Thread)dispatcher.getDronesList().get(321)).getName());

    }

    @Test(expected = DuplicateDronPIDException.class)
    public void dispatcherNoTwoDronesSamePid()
            throws DuplicateDronPIDException {
        Dispatcher dispatcher = new Dispatcher();
        int pid1 = 123;

        Dron dronA = new Dron(pid1);

        dispatcher.addDron(dronA);
        dispatcher.addDron(dronA);

    }

    @Test
    public void dispatcherReadCsvDronesFromPath()
            throws NonCSVFileFoundException, UnsupportedCSVFileException {
        Dispatcher dispatcher = new Dispatcher();
        int pid1 = 123;

        Dron dronA = new Dron(pid1);

        List<DronOrder> dronOrderList = dispatcher.readCsvDronOrdersFile(dronA);

        Assert.assertEquals(2, dronOrderList.size());
        Assert.assertEquals(123, dronOrderList.get(0).getPid());
        Assert.assertEquals(123, dronOrderList.get(1).getPid());
        assertThat(51.476105,
                closeTo(dronOrderList.get(0).getLongitude(), DELTA));
        assertThat(-0.100224,
                closeTo(dronOrderList.get(0).getLatitude(), DELTA));
        Assert.assertEquals(Timestamp.valueOf("2011-03-22 07:55:26"),
                dronOrderList.get(0).getTime());
    }

    @Test
    public void deleteDronFromList() throws DuplicateDronPIDException {

        Dispatcher dispatcher = new Dispatcher();
        int pid1 = 123;
        int pid2 = 321;

        Dron dronA = new Dron(pid1);
        Dron dronB = new Dron(pid2);
        dispatcher.addDron(dronA);
        dispatcher.addDron(dronB);

        assertEquals(2, dispatcher.getDronesList().size());

        dispatcher.deleteDron(pid1);

        assertEquals(1, dispatcher.getDronesList().size());
        assertEquals(dronB, dispatcher.getDronesList().get(321));

    }


    @Test
    public void stopAllDronesThreadInstances()
            throws DuplicateDronPIDException {

        Dispatcher dispatcher = new Dispatcher();
        int pid1 = 123;
        int pid2 = 321;

        Dron dronA = new Dron(pid1);
        Dron dronB = new Dron(pid2);
        dispatcher.addDron(dronA);
        dispatcher.addDron(dronB);

        assertEquals(2, dispatcher.getDronesList().size());
        dispatcher.stopAllDrones();
        assertEquals(0, dispatcher.getDronesList().size());

    }









}
