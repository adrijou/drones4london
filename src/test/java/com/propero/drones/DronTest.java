package com.propero.drones;

import com.propero.drones.constants.Constants;
import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileException;
import com.propero.drones.pojo.DronOrder;
import com.propero.drones.pojo.TubeStation;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.concurrent.ArrayBlockingQueue;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 07/03/16<br/>
 */
public class DronTest {

    private static final double LATITUDE_INIT = 51.508066;
    private static final double LONGITUDE_INIT = -0.163169;
    private static final double DELTA = 1e-15;


    @Test
    public void dronClassConstructor() throws ParseException {
        Dispatcher dispatcher = new Dispatcher();
        int pid = 123;
        long time = Timestamp.valueOf(Constants.TIME_START_WORKING).getTime();
        Dron dron = new Dron(pid, dispatcher);

        assertEquals(123, dron.getPid());
        assertEquals(dispatcher, dron.getServerDispatcher());
        assertThat(LATITUDE_INIT, closeTo(dron.getCurrentPoint().getLatitude(),
                DELTA));
        assertThat(LONGITUDE_INIT, closeTo(dron.getCurrentPoint().getLongitude(),
                DELTA));
        assertEquals(time, dron.getCurrentPoint().getTime());
    }

    @Test
    public void dronConstructorNonDefaultTimeStart() throws ParseException {
        Dispatcher dispatcher = new Dispatcher();
        int pid = 123;
        Dron dron = new Dron(pid, dispatcher);

        assertEquals(Timestamp.valueOf(Constants.TIME_START_WORKING).getTime(),
                dron.getCurrentPoint().getTime());
    }

    @Test
    public void dronTestDistanceBetweenVauxhalAndVictoria()
            throws ParseException {
        //Vauxhall
        double lat1 = 51.484833;
        double lon1 = -0.126416;
        //Victoria
        double lat2 = 51.496424;
        double lon2 = -0.143921;

        Dron dron = new Dron(123, new Dispatcher());

        assertThat(2.0994673276807613,
                closeTo(dron.distance(lat1, lon1, lat2, lon2), DELTA));
    }

    @Test
    public void findingVictoriaStationPointLessThan350MetersDistance()
            throws ParseException, NonCSVFileFoundException, UnsupportedCSVFileException {

        Dispatcher dispatcherServer = new Dispatcher();
        Dron dron = new Dron(123, dispatcherServer);

        DronOrder order = new DronOrder(dron.getPid());
        //Google Corporate office: 51.493553, -0.146464
        order.setLatitude(51.496424);
        order.setLongitude(-0.143921);

        TubeStation tube = dron.findTubeStationInArea(order);

        assertNotNull(tube);
        assertEquals("Victoria", tube.getTubeName());


    }

    @Test
    public void nonFindingAnyStationGivenOutsideCoodinatesPoint()
            throws ParseException, NonCSVFileFoundException, UnsupportedCSVFileException {
        Dispatcher dispatcherServer = new Dispatcher();
        Dron dron = new Dron(123, dispatcherServer);

        DronOrder order = new DronOrder(dron.getPid());
        //Google Propero Marbella office: 36.510167, -4.889600
        order.setLatitude(36.510167);
        order.setLongitude(-4.889600);

        TubeStation tube = dron.findTubeStationInArea(order);

        assertNull(tube);
    }

    @Test
    public void moveToNextCoordinateDron() throws ParseException,
                                                InterruptedException {
        Dispatcher dispatcherServer = new Dispatcher();
        Dron dron = new Dron(123, dispatcherServer);

        //Vauxhall
        DronOrder startPoint = new DronOrder(dron.getPid());
        startPoint.setLatitude(51.484833);
        startPoint.setLongitude(-0.126416);
        startPoint.setTime(Timestamp.valueOf("2011-03-22 07:48:29").getTime());
        //Victoria
        DronOrder endPoint = new DronOrder(dron.getPid());
        endPoint.setLatitude(51.496424);
        endPoint.setLongitude(-0.143921);
        endPoint.setTime(Timestamp.valueOf("2011-03-22 07:49:29").getTime());

        double distance = dron.moveToNextCoordinate(endPoint);

        assertThat(2.249492538329579, closeTo(distance, DELTA));
        assertThat(51.496424, closeTo(dron.getCurrentPoint().getLatitude(), DELTA));
        assertThat(-0.143921, closeTo(dron.getCurrentPoint().getLongitude(), DELTA));
    }

}
