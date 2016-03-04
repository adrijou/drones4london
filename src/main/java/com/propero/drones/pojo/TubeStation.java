package com.propero.drones.pojo;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public class TubeStation {

    private String tubeName;
    private Double longitude;
    private Double latitude;


    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(final Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(final Double latitude) {
        this.latitude = latitude;
    }

    public String getTubeName() {
        return tubeName;
    }

    public void setTubeName(final String tubeName) {
        this.tubeName = tubeName;
    }


}
