package producerconsumer;

public class ProducerConsumerTest {
    public static void main(String[] args) {
        ProducerConsumerMonitor<Integer> monitor = new ProducerConsumerMonitor<>(10);
        for (int i = 0; i < 3; i++) {
            new Producer(i, monitor).start();
        }
        for (int i = 0; i < 3; i++) {
            new Consumer(i, monitor).start();
        }
    }
}
