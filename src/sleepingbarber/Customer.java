package sleepingbarber;

/**
 * Represents a customer in the Sleeping barber problem
 */
public class Customer extends Thread {
    private final SleepingBarberMonitor monitor;
    private final int id;

    public Customer(int id, SleepingBarberMonitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        monitor.enterBarberShop(id);
        monitor.getHairCut(id);
    }
}
