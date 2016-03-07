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
    private Timestamp timeStamp;
    private double speed;
    private TrafficConditions trafficConditions;

    public TrafficReport(final int pid, final Timestamp timeStamp) {
        this.dronID = pid;
        this.timeStamp = timeStamp;
        TrafficConditions[] tc = TrafficConditions.values();
        Random random = new Random();
        int index = random.nextInt(tc.length);
        this.trafficConditions = tc[index];
    }

    public int getDronID() {
        return dronID;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
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
            final TrafficConditions trafficConditions) {
        this.trafficConditions = trafficConditions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Dron: ");
        sb.append(dronID).append("\nTime: ").append(timeStamp)
                .append("\nSpeed: ").append(speed)
                .append("\nTraffic conditions: ").append(trafficConditions);
        return sb.toString();
    }
}
