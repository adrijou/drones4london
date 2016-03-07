package com.propero.drones;

import com.propero.drones.pojo.DronOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 07/03/16<br/>
 */
public class OrdersQueue {

    private static final Logger LOG
            = LoggerFactory.getLogger(OrdersQueue.class);

    private Vector vector = new Vector();
    private boolean stopWaiting = false;
    private boolean waiting = false;
    private static final int MEM_SIZE = 10;


    public synchronized void setSize(final int size) {
        vector.setSize(size);
    }

    /**
     * Put the object into the queue.
     * @param object the object to be appended to the queue.
     */
    public synchronized void put(final Object object) {
        if (vector.size() == MEM_SIZE - 1) {
            try {
                waiting = true;
                wait();
            } catch (InterruptedException ex) {
                LOG.info("InterruptedException" + ex);
            }
        }
        vector.addElement((DronOrder) object);
        notify();
    }

    /** Break the pull(), allowing the calling thread to exit
     */
    public synchronized void stop() {
        stopWaiting = true;
        // just a hack to stop waiting
        if (waiting) {
            notify();
        }
    }

    /**
     * Pull the first object out of the queue. Wait if the queue is
     * empty.
     */
    public synchronized Object pull() {
        while (isEmpty()) {
            try {
                waiting = true;
                wait();
            } catch (InterruptedException ex) {
                LOG.info("InterruptedException" + ex);
            }
           /* waiting = false;
            if (stopWaiting) {
                return null;
            }*/
        }
        return get();
    }

    /**
     * Get the first object out of the queue. Return null if the queue
     * is empty.
     */
    public synchronized Object get() {
        Object object = peek();
        if (object != null) {
            vector.removeElementAt(0);
        }
        notify();
        return object;
    }

    /**
     * Peek to see if something is available.
     */
    public Object peek() {
        if (isEmpty()) {
            return null;
        }
        return vector.elementAt(0);
    }

    /**
     * Is the queue empty?
     */
    public boolean isEmpty() {
        return vector.isEmpty();
    }

    /**
     * How many elements are there in this queue?
     */
    public int size() {
        return vector.size();
    }

}
