package com.core.readerwriter;

import com.core.monitor.Monitor;
import com.core.readerwriter.gui.RWListener;
import java.util.concurrent.locks.Condition;

public class ReaderWriterMonitor extends Monitor {
    private int numOfReaders = 0;
    private int numOfWriters = 0;
    private int waitingWriters = 0;
    private final Condition canWrite;
    private final Condition canRead;
    private final RWListener listener;

    public ReaderWriterMonitor(RWListener listener) {
        this.listener = listener;
        this.canWrite = lock.newCondition();
        this.canRead = lock.newCondition();
    }

    public void startWriting(int id) {
        enter(id);
        try {
            waitingWriters++;
            if (listener != null)
                listener.onWriterWaiting(id);
            while (numOfReaders > 0 || numOfWriters > 0) {
                canWrite.await();
            }
            waitingWriters--;
            numOfWriters++;
            if (listener != null)
                listener.onWriterStart(id);
        } catch (InterruptedException e) {
            waitingWriters--;
            Thread.currentThread().interrupt();
        } finally {
            exit(id);
        }
    }

    public void stopWriting(int id) {
        enter(id);
        try {
            numOfWriters--;
            if (listener != null)
                listener.onWriterStop(id);
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
            if (listener != null)
                listener.onReaderWaiting(id);
            while (numOfWriters > 0) {
                canRead.await();
            }
            numOfReaders++;
            if (listener != null)
                listener.onReaderStart(id);
            canRead.signalAll(); // Đánh thức các reader khác
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            exit(id);
        }
    }

    public void stopReading(int id) {
        enter(id);
        try {
            numOfReaders--;
            if (listener != null)
                listener.onReaderStop(id);
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