package com.propero.drones.exceptions;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public final class UnsupportedCSVFileException extends DronException {

    public UnsupportedCSVFileException(final String nameFile) {
        super(nameFile + " contains wrong format parameters");
    }

    public UnsupportedCSVFileException(final String nameFile,
                                       final String message) {
        super(nameFile + " contains wrong format parameters."
                        + message);
    }

}
