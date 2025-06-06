package com.core.sleepingbarber;

public class Barber extends Thread {
    private final SleepingBarberMonitor monitor;

    public Barber(SleepingBarberMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            monitor.cutHair(); // Chỉ cần gọi phương thức này
        }
    }
}