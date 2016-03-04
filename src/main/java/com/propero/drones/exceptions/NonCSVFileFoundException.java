package com.propero.drones.exceptions;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public final class NonCSVFileFoundException extends DronException {

    public NonCSVFileFoundException(final String nameFile) {
        super("The CSV file " + nameFile
                + " was not found in the defined path");
    }
}
