package com.core.producerconsumer;

import com.core.monitor.Monitor;
import com.core.producerconsumer.gui.ProducerConsumerListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;

public class ProducerConsumerMonitor<T> extends Monitor {
    private final Queue<T> buffer = new LinkedList<>();
    private final int bufferSize;
    private final Condition notFull;
    private final Condition notEmpty;
    private final ProducerConsumerListener<T> listener; // Thêm listener

    // Sửa constructor để nhận listener
    public ProducerConsumerMonitor(int bufferSize, ProducerConsumerListener<T> listener) {
        this.bufferSize = bufferSize;
        this.listener = listener;
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }

    public void produce(T item, int id) {
        enter(id);
        try {
            while (buffer.size() == bufferSize) {
                if (listener != null)
                    listener.onProducerWaiting(id);
                notFull.await();
                if (listener != null)
                    listener.onProducerResumed(id);
            }
            buffer.add(item);
            if (listener != null) {
                listener.logMessage("Producer " + id + " produced " + item);
                listener.onProduce(id, item);
            }
            notEmpty.signal();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            exit(id);
        }
    }

    public T consume(int id) {
        enter(id);
        try {
            while (buffer.isEmpty()) {
                if (listener != null)
                    listener.onConsumerWaiting(id);
                notEmpty.await();
                if (listener != null)
                    listener.onConsumerResumed(id);
            }
            T item = buffer.poll();
            if (listener != null) {
                listener.logMessage("Consumer " + id + " is consuming " + item);
                listener.onConsume(id, item);
            }
            notFull.signal();
            return item;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
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