package com.propero.drones.pojo;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public final class TubeStation extends Coordinates {

    private String tubeName;

    public String getTubeName() {
        return tubeName;
    }

    public void setTubeName(final String tubeName) {
        this.tubeName = tubeName;
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
