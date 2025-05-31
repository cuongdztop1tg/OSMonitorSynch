package diningphilosopher;

/**
 * Represents a Philosopher thread in the Dining Philosopher problem.
 */
public class Philosopher extends Thread {
    private final int id;
    private final DiningPhilosopherMonitor monitor;

    public Philosopher(int id, DiningPhilosopherMonitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    /**
     * Simulates the thinking process of a philosopher by logging the action
     * and making the current thread sleep for a random duration.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    public void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking");
        Thread.sleep(Math.round(Math.random() * 5000));
    }

    /**
     * Logs the action of a philosopher eating and simulates the time taken for eating
     * by making the current thread sleep for a random duration.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    public void eat() throws InterruptedException {
        System.out.println("Philosopher " + id + " is eating");
        Thread.sleep(Math.round(Math.random() * 2000));
    }

    @Override
    public void run() {
        while (true) {
            try {
                think();
                monitor.enter(id);
                eat();
                monitor.exit(id);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
