package com.core.readerwriter;

public class Reader extends Thread {
    private final ReaderWriterMonitor monitor;
    private final int id;

    public Reader(int id, ReaderWriterMonitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            // Thay đổi điều kiện vòng lặp
            while (!Thread.currentThread().isInterrupted()) {
                monitor.startReading(id);
                Thread.sleep(Math.round(Math.random() * 5000));
                monitor.stopReading(id);
                Thread.sleep(Math.round(Math.random() * 5000));
            }
        } catch (InterruptedException e) {
            // Khi sleep() hoặc await() bị ngắt, nó sẽ ném ra ngoại lệ này.
            // Luồng sẽ tự động thoát khỏi vòng lặp.
            System.out.println("Reader " + id + " was interrupted and is shutting down.");
            // Good practice: Restore the interrupted status
            Thread.currentThread().interrupt();
        }
        System.out.println("Reader " + id + " has stopped.");
    }
}