package com.propero.drones.exceptions;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public final class UnsupportedCSVFileExceptionImpl extends DronException {

    public UnsupportedCSVFileExceptionImpl(final String nameFile) {
        super("The CSV file " + nameFile + " contains wrong format parameters");
    }

    public UnsupportedCSVFileExceptionImpl(final String nameFile,
                                           final String message) {
        super("The CSV file " + nameFile + " contains wrong format parameters."
                        + message);
    }

}
