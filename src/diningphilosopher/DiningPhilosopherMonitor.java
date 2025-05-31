package diningphilosopher;

import monitor.Monitor;

import java.util.concurrent.locks.Condition;

/**
 * - Implementation of the Monitor of the Dining Philosopher problem. <br>
 * - Inherits from {@link monitor.Monitor}
 */
public class DiningPhilosopherMonitor extends Monitor {
    public enum State {EATING, THINKING, HUNGRY};
    private final State[] states;
    private final int numOfPhilosophers;
    private final Condition[] self;

    public DiningPhilosopherMonitor(int numOfPhilosophers) {
        this.numOfPhilosophers = numOfPhilosophers;
        this.states = new State[numOfPhilosophers];
        this.self = new Condition[numOfPhilosophers];
        for (int i = 0; i < numOfPhilosophers; i++) {
            states[i] = State.THINKING;
            self[i] = lock.newCondition();
        }
    }

    /**
     * Finds the id of the philosopher sitting on the left
     * @param id ID of the current philosopher
     * @return id of the philosopher sitting on the left
     */
    private int left(int id) {
        return (id + numOfPhilosophers - 1) % numOfPhilosophers;
    }

    /**
     * Finds the id of the philosopher sitting on the right.
     * @param id ID of the current philosopher
     * @return id of the philosopher sitting on the right
     */
    private int right(int id) {
        return (id + 1) % numOfPhilosophers;
    }

    /**
     * Set the state of the philosopher with id=id to eating
     * if the left and right philosopher is not eating
     * @param id The current philosopher
     */
    private void test(int id) {
        if (states[id] == State.HUNGRY && states[left(id)] != State.EATING && states[right(id)] != State.EATING) {
            states[id] = State.EATING;
            self[id].signal();
        }
    }

    @Override
    public void enter(int id) {
        lock.lock();
        try {
            states[id] = State.HUNGRY;
            test(id);
            if (states[id] != State.EATING) {
                self[id].await(); // Wait for the chopsticks
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void exit(int id) {
        lock.lock();
        try {
            states[id] = State.THINKING;
            test(left(id));
            test(right(id));
        } finally {
            lock.unlock();
        }
    }
}
