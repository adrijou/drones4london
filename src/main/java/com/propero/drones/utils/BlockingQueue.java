package com.propero.drones.utils;

import com.propero.drones.pojo.DronOrder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 07/03/16<br/>
 */
public class BlockingQueue {

    private static final int LIMIT_QUEUE_DEFAULT = 5;

    private List queue = new LinkedList();
    //By default 5, unless we set the limit via constructor.
    private int  limit = LIMIT_QUEUE_DEFAULT;

    public BlockingQueue(final int limit) {
        this.limit = limit;
    }


    public synchronized void enqueue(final DronOrder item)
            throws InterruptedException  {
        while (this.queue.size() == this.limit) {
            wait();
        }
        if (this.queue.size() == 0) {
            notifyAll();
        }
        this.queue.add(item);
    }


    public synchronized DronOrder dequeue()
            throws InterruptedException {
        while (this.queue.size() == 0) {
            wait();
        }
        if (this.queue.size() == this.limit) {
            notifyAll();
        }

        return (DronOrder) this.queue.remove(0);
    }

}
