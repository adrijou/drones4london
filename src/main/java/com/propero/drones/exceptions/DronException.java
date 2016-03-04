package com.propero.drones.exceptions;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public abstract class DronException extends Exception {

    public DronException(final String nameFile) {
        super("Exception in drones4london was found");
    }

}
