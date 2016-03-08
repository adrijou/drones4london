package com.propero.drones;

import com.propero.drones.pojo.TrafficReport;
import org.junit.Test;

import java.sql.Timestamp;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public class TrafficReportTest {

    private static final double DELTA = 1e-15;

    @Test
    public void trafficReportAsPojo() {

        int pid = 123;
        long time =Timestamp.valueOf("2016-03-03 23:30:10.0").getTime();
        double speed = 123.123;
        String tubeSt = "Victoria";

        TrafficReport trafficReport = new TrafficReport(pid, tubeSt,time, speed);

        trafficReport.setSpeed(12.50);
        trafficReport.setTrafficConditions(
                TrafficReport.TrafficConditions.MODERATE);

        assertEquals(123, trafficReport.getDronID());
        assertThat(12.50, closeTo(trafficReport.getSpeed(), DELTA));
        assertEquals(time, trafficReport.getTime());
        assertEquals(TrafficReport.TrafficConditions.MODERATE,
                trafficReport.getTrafficConditions());
    }

    @Test
    public void trafficReportRandom() {
        int pid = 123;
        double speed = 123.123;
        String tubeSt = "Victoria";
        Timestamp time =Timestamp.valueOf("2016-03-03 23:30:10.0");

        TrafficReport tr = new TrafficReport(pid, tubeSt, time.getTime(), speed);

        assertNotNull(tr.getTrafficConditions());

    }


}
