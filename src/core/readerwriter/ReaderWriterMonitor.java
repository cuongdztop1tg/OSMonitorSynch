package core.readerwriter;

import core.monitor.Monitor;
import java.util.concurrent.locks.Condition;

public class ReaderWriterMonitor extends Monitor {
    private int numOfReaders = 0;
    private int numOfWriters = 0;
    private int waitingWriters = 0;
    private final Condition canWrite;
    private final Condition canRead;

    public ReaderWriterMonitor() {
        this.canWrite = lock.newCondition();
        this.canRead = lock.newCondition();
    }

    public void startWriting(int id) {
        enter(id);
        try {
            waitingWriters++;
            while (numOfReaders > 0 || numOfWriters > 0) {
                canWrite.await();
            }
            waitingWriters--;
            numOfWriters++;
        } catch (InterruptedException e) {
            waitingWriters--;
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted", e);
        } finally {
            exit(id);
        }
    }

    public void stopWriting(int id) {
        enter(id);
        try {
            numOfWriters--;
            // Prioritize writers to prevent starvation
            if (waitingWriters > 0) {
                canWrite.signal();
            } else {
                canRead.signalAll();
            }
        } finally {
            exit(id);
        }
    }

    public void startReading(int id) {
        enter(id);
        try {
            while (numOfWriters > 0 || waitingWriters > 0) {
                canRead.await();
            }
            numOfReaders++;
            // Allow other readers to enter
            canRead.signal();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted", e);
        } finally {
            exit(id);
        }
    }

    public void stopReading(int id) {
        enter(id);
        try {
            numOfReaders--;
            if (numOfReaders == 0) {
                canWrite.signal();
            }
        } finally {
            exit(id);
        }
    }

    @Override
    public void enter(int id) {
        lock.lock();
    }

    @Override
    public void exit(int id) {
        lock.unlock();
    }
}