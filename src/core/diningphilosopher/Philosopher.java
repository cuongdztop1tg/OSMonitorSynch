package core.diningphilosopher;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a Philosopher thread in the Dining Philosopher problem.
 */
public class Philosopher extends Thread {
    private final int id;
    private final PhilosopherStateListener listener;
    private final DiningPhilosophersMonitor monitor;
    private volatile boolean running = true;

    public Philosopher(int id, PhilosopherStateListener listener, DiningPhilosophersMonitor monitor) {
        this.id = id;
        this.listener = listener;
        this.monitor = monitor;
    }

    /**
     * Simulates the thinking process of a philosopher by logging the action
     * and making the current thread sleep for a random duration.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    public void think() throws InterruptedException {
        listener.onThinking(id);
        System.out.println("Philosopher " + id + " is thinking");
        Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5001));
    }

    /**
     * Logs the action of a philosopher eating and simulates the time taken for eating
     * by making the current thread sleep for a random duration.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    public void eat() throws InterruptedException {
        listener.onEating(id);
        System.out.println("Philosopher " + id + " is eating");
        Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5001));
    }

    @Override
    public void run() {
        while (running) {
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

    public void stopPhilosopher() {
        listener.onThinking(id);
        System.out.println("Philosopher " + id + " stops");
        running = false;
        this.interrupt();
    }
}
