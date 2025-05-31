package producerconsumer;

/**
 * Represents a producer thread in the Producer-Consumer problem.
 */
public class Producer extends Thread {
    private final ProducerConsumerMonitor<Integer> monitor;
    private final int id;

    public Producer(int id, ProducerConsumerMonitor<Integer> monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        int item = 0;
        try {
            while (true) {
                monitor.produce(item++, id);
                Thread.sleep(Math.round(Math.random() * 5000));
            }
        } catch (InterruptedException e) {
            System.out.println("Producer " + id + " interrupted");
        }
    }
}
