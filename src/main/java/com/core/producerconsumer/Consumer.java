package com.core.producerconsumer;

import com.core.producerconsumer.gui.ProducerConsumerListener;

public class Consumer extends Thread {
    private final ProducerConsumerMonitor<Integer> monitor;
    private final int id;
    private final ProducerConsumerListener<Integer> listener;

    public Consumer(int id, ProducerConsumerMonitor<Integer> monitor, ProducerConsumerListener<Integer> listener) {
        this.id = id;
        this.monitor = monitor;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            // Thay đổi điều kiện vòng lặp
            while (!Thread.currentThread().isInterrupted()) {
                Integer item = monitor.consume(id);
                if (item != null) {
                    Thread.sleep(Math.round(Math.random() * 4000) + 6000);
                    if (listener != null) {
                        listener.logMessage("Consumer " + id + " finished consuming: " + item);
                        listener.onConsumerResumed(id);
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Consumer " + id + " was interrupted and is shutting down.");
            Thread.currentThread().interrupt();
        }
        System.out.println("Consumer " + id + " has stopped.");
    }
}