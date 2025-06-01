package core.producerconsumer;

import core.monitor.Monitor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;

/**
 * The ProducerConsumerMonitor class provides a thread-safe mechanism based on the Monitor pattern
 * to enable producers and consumers to concurrently add and retrieve elements from a shared buffer.
 * This class ensures synchronized access to the buffer, preventing race conditions and allowing
 * proper coordination between producer and consumer threads.
 *
 * @param <T> The type of objects that the buffer will store.
 */
public class ProducerConsumerMonitor<T> extends Monitor {
    private final Queue<T> buffer = new LinkedList<>();
    private final int bufferSize;
    private final Condition notFull;
    private final Condition notEmpty;

    public ProducerConsumerMonitor(int bufferSize) {
        this.bufferSize = bufferSize;
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }

    /**
     * Adds an item to the shared buffer, ensuring that the operation is thread-safe.
     * If the buffer is full, the producer thread will wait until space becomes available.
     * Once the item is added, the consumer threads are notified that the buffer is not empty.
     *
     * @param item The item to be added to the buffer.
     * @param id The unique identifier of the producer thread performing the operation.
     */
    public void produce(T item, int id) {
        enter(id);
        try {
            while (buffer.size() == bufferSize) {
                notFull.await();
            }
            buffer.add(item);
            System.out.println("Producer " + id + " produced " + item);
            notEmpty.signal();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            exit(id);
        }
    }

    /**
     * Retrieves and removes the head of the shared buffer in a thread-safe manner.
     * If the buffer is empty, the consuming thread will wait until an item becomes available.
     * Once an item is retrieved, producer threads are notified that there is space in the buffer.
     *
     * @param id The unique identifier of the consumer thread performing the operation.
     * @return The item consumed from the buffer, or null if the thread was interrupted.
     */
    public T consume(int id) {
        enter(id);
        try {
            while (buffer.isEmpty()) {
                notEmpty.await();
            }
            T item = buffer.poll();
            System.out.println("Consumer " + id + " consumed: " + item);
            notFull.signal();
            return item;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            exit(id);
        }
    }

    @Override
    public void enter(int id) {
        lock.lock();
    }

    @Override
    public void exit(int id) {
        lock.unlock();
    }
}
