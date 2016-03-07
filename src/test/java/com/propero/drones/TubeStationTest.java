package com.propero.drones;

import com.propero.drones.pojo.TubeStation;
import org.junit.Test;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public class TubeStationTest {

    private static final double DELTA = 1e-15;

    @Test
    public void tubesCoordinatesAsPojo() {
        TubeStation coordinatesTube = new TubeStation();

        coordinatesTube.setTubeName("Victoria");
        coordinatesTube.setLatitude(51.496424);
        coordinatesTube.setLongitude(-0.143921);

        assertEquals("Victoria", coordinatesTube.getTubeName());
        assertThat(51.496424, closeTo(coordinatesTube.getLatitude(), DELTA));
        assertThat(-0.143921, closeTo(coordinatesTube.getLongitude(), DELTA));
    }

}
