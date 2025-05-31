package diningphilosopher;

public class DiningPhilosopherTest {
    public static void main(String[] args) {
        int numOfPhilosophers = 5;
        DiningPhilosopherMonitor monitor = new DiningPhilosopherMonitor(numOfPhilosophers);
        Philosopher[] philosophers = new Philosopher[numOfPhilosophers];
        for (int id = 0; id < 5; id++) {
            philosophers[id] = new Philosopher(id, monitor);
            philosophers[id].start();
        }
    }
}
