package com.propero.drones.pojo;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public final class DronOrder {

    private int pid;
    private String latitude;
    private String longitude;
    private String time;

    public DronOrder(final int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(final String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(final String longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(final String time) {
        this.time = time;
    }
}
