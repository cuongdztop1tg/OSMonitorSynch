package com.core.readerwriter;

public class Writer extends Thread {
    private final ReaderWriterMonitor monitor;
    private final int id;

    public Writer(int id, ReaderWriterMonitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            // Thay đổi điều kiện vòng lặp
            while (!Thread.currentThread().isInterrupted()) {
                monitor.startWriting(id);
                Thread.sleep(Math.round(Math.random() * 5000));
                monitor.stopWriting(id);
                Thread.sleep(Math.round(Math.random() * 5000));
            }
        } catch (InterruptedException e) {
            System.out.println("Writer " + id + " was interrupted and is shutting down.");
            Thread.currentThread().interrupt();
        }
        System.out.println("Writer " + id + " has stopped.");
    }
}