package com.propero.drones.pojo;

import java.sql.Timestamp;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public class TrafficReport {


    public enum TrafficConditions  { HEAVY, LIGHT, MODERATE };

    private int dronID;
    private long time;
    private double speed;
    private TrafficConditions trafficConditions;
    private String tubeStation;

    public TrafficReport(final int pid, final String tubeStation,
                         final long time, final double speed) {
        this.dronID = pid;
        this.time = time;
        TrafficConditions[] tc = TrafficConditions.values();
        Random random = new Random();
        int index = random.nextInt(tc.length);
        this.trafficConditions = tc[index];
        this.speed = speed;
        this.tubeStation = tubeStation;
    }

    public int getDronID() {
        return dronID;
    }

    public long getTime() {
        return time;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(final double speed) {
        this.speed = speed;
    }

    public TrafficConditions getTrafficConditions() {
        return trafficConditions;
    }

    public void setTrafficConditions(
            final    TrafficConditions trafficConditions) {
        this.trafficConditions = trafficConditions;
    }

    public String getTubeStation() {
        return tubeStation;
    }

    public void setTubeStation(final String tubeStation) {
        this.tubeStation = tubeStation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Dron: ");
        sb.append(dronID).append("\nTube Station: ").append(tubeStation)
                .append("\nTime: ").append(new Timestamp(time))
                .append("\nSpeed: ").append(speed)
                .append("\nTraffic conditions: ").append(trafficConditions);
        return sb.toString();
    }
}
