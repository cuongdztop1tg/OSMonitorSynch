package com.core.sleepingbarber;

public class Customer extends Thread {
    private final SleepingBarberMonitor monitor;
    private final int id;

    public Customer(int id, SleepingBarberMonitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        monitor.requestHaircut(id);
    }
}