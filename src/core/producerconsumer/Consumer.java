package core.producerconsumer;

/**
 * Represents a consumer thread in the Producer-Consumer problem.
 */
public class Consumer extends Thread {
    private final ProducerConsumerMonitor<Integer> monitor;
    private final int id;

    public Consumer(int id, ProducerConsumerMonitor<Integer> monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer item = monitor.consume(id);
                System.out.println("Consumer " + id + " consumed: " + item);
                Thread.sleep(Math.round(Math.random() * 5000)); // Mô phỏng thời gian tiêu thụ
            }
        } catch (InterruptedException e) {
            System.out.println("Consumer " + id + " interrupted.");
        }
    }
}
