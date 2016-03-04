package com.propero.drones.pojo;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public class TrafficReport {

    public enum TrafficConditions  { HEAVY, MODERATE, LIGHT };

    private int dronID;
    private Timestamp timeStamp;
    private double speed;
    private TrafficConditions trafficConditions;

    public int getDronID() {
        return dronID;
    }

    public void setDronID(final int dronID) {
        this.dronID = dronID;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(final Timestamp timeStamp) {
        this.timeStamp = timeStamp;
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
}
