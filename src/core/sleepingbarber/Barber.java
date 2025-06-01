package core.sleepingbarber;

/**
 * Represents the barber in the Sleeping barber problem
 */
public class Barber extends Thread {
    private final SleepingBarberMonitor monitor;

    public Barber(SleepingBarberMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            monitor.serveCustomer();
        }
    }
}
