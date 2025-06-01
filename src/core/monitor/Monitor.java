package core.monitor;

import java.util.concurrent.locks.ReentrantLock;

/**
 * - Abstract class representing a Monitor for synchronizing thread access to shared resources. <br>
 * - Uses a ReentrantLock to ensure mutual exclusion in the critical section. <br>
 * - Subclasses must provide implementations for thread entry and exit methods.
 */
public abstract class Monitor {
    protected final ReentrantLock lock = new ReentrantLock();

    /**
     * Method for a thread with id=id to enter the critical section
     * @param id ID of the thread
     */
    public abstract void enter(int id);

    /**
     * Method for a thread with id=id to exit the critical section
     * @param id ID of the thread
     */
    public abstract void exit(int id);
}
