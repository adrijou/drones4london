package com.propero.drones;

import com.propero.drones.pojo.DronOrder;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public class DronOrderTest {

    private static final double DELTA = 1e-15;

    @Test
    public void newDronOrderAsPojoObject() {
        assertEquals(123, new DronOrder(123).getPid());
    }

    @Test
    public void dronOrderAsPojo() throws ParseException {
        int pid = 123;
        DronOrder coordinates = new DronOrder(pid);

        coordinates.setLatitude(51.476105);
        coordinates.setLongitude(-0.100224);

        String ts = "2016-03-02 20:01:10.0";

        coordinates.setTime(Timestamp.valueOf(ts).getTime());

        assertEquals(123, coordinates.getPid());
        assertThat(51.476105, closeTo(coordinates.getLatitude(), DELTA));
        assertThat(-0.100224, closeTo(coordinates.getLongitude(), DELTA));

        assertEquals(Timestamp.valueOf(ts).getTime(), coordinates.getTime());
    }

    @Test
    public void dronOrderCustomTimeToStartWorking() throws ParseException {
        int pid = 123;
        DronOrder coordinates = new DronOrder(pid);


        String ts = "2016-03-02 20:01:10.0";
        coordinates.setTime(Timestamp.valueOf(ts).getTime());

        assertEquals(Timestamp.valueOf(ts).getTime(), coordinates.getTime());
    }

}
