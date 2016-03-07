package com.propero.drones.exceptions;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 04/03/16<br/>
 */
public final class DuplicateDronPIDException extends DronException {

    public DuplicateDronPIDException(final int pid) {
        super(pid + " already in use.");
    }
}
