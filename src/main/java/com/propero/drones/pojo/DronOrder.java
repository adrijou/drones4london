package com.propero.drones.pojo;


import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public final class DronOrder extends Coordinates {

    private int pid;
    private Timestamp time;

    public DronOrder(final int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(final Timestamp time) {
        this.time = time;
    }

    public double getLongitude() {
        return super.getLongitude();
    }

    public void setLongitude(final double longitude) {
        super.setLongitude(longitude);
    }


    public double getLatitude() {
        return super.getLatitude();
    }

    public void setLatitude(final double latitude) {
        super.setLatitude(latitude);
    }



}
