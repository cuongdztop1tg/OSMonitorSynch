package com.core.producerconsumer;

public class Producer extends Thread {
    private final ProducerConsumerMonitor<Integer> monitor;
    private final int id;

    public Producer(int id, ProducerConsumerMonitor<Integer> monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        int item = 0;
        try {
            // Thay đổi điều kiện vòng lặp
            while (!Thread.currentThread().isInterrupted()) {
                monitor.produce(item++, id);
                Thread.sleep(Math.round(Math.random() * 5000) + 5000);
            }
        } catch (InterruptedException e) {
            System.out.println("Producer " + id + " was interrupted and is shutting down.");
            Thread.currentThread().interrupt(); // Khôi phục trạng thái ngắt
        }
        System.out.println("Producer " + id + " has stopped.");
    }
}