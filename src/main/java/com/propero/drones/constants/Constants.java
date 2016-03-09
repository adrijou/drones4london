package com.propero.drones.constants;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 08/03/16<br/>
 */
public final class Constants {

    //avoiding magic numbers
    public static final double RADIO_IN_KMS = 0.350;
    public static final double LATITUDE_INIT = 51.508066;
    public static final double LONGITUDE_INIT = -0.163169;
    public static final int MEM_SIZE = 10;
    public static final int THREE = 3;
    public static final int CONVERT_TO_KMS = 100;
    public static final int TWO_HUNDRED_FORTY = 240;
    public static final int SEVEN = 7;
    public static final int FORTY_FIVE = 45;
    public static final int HUNDRED_K = 100000;




    public static final int DRON_1 = 5937;
    public static final int DRON_2 = 6043;
    public static final String TIME_START_WORKING = "2011-03-22 07:45:00";
    //07:45:00 -> 08:10 = 25' in milliseconds
    public static final long STOP_AT_8_10 = 1500000; //25' in milliseconds

    private Constants() {
        throw new AssertionError();
    }



}
