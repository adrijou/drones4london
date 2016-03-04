package com.propero.drones;

import com.propero.drones.pojo.DronOrder;
import org.junit.Test;

import java.text.ParseException;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public class DronOrderTest {

    @Test
    public void newDronOrderAsPojoObject() {
        assertEquals(123, new DronOrder(123).getPid());
    }

    @Test
    public void dronOrderAsPojo() throws ParseException {
        int pid = 123;
        DronOrder coordinates = new DronOrder(pid);

        coordinates.setLongitude("-0.100224");
        coordinates.setLatitude("51.476105");

        String ts = "2016-03-02 20:01:10";

        coordinates.setTime(ts);

        assertEquals(123, coordinates.getPid());
        assertEquals("-0.100224", coordinates.getLongitude());
        assertEquals("51.476105", coordinates.getLatitude());
        assertEquals("2016-03-02 20:01:10", coordinates.getTime());
    }

    @Test
    public void coordinatesLongitudeAsDouble() {
        //TODO
        assertNull(null);
    }

    @Test
    public void coordinatesLatitudeAsDouble() {
        //TODO
        assertNull(null);
    }

    @Test
    public void coordinatesTimeAsTimestamp() {
        //TODO
        assertNull(null);
    }

}
