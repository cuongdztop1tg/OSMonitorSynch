package core.sleepingbarber;

import core.monitor.Monitor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;

/**
 * The SleepingBarberMonitor class provides a thread-safe mechanism based on the Monitor pattern
 * to enable barbers to concurrently cut hair for customers in a shared waiting room.
 * This class ensures synchronized access to the waiting room, preventing race conditions and allowing
 * proper coordination between barber and customer threads.
 */
public class SleepingBarberMonitor extends Monitor {
    private final int MAX_NUM_OF_CHAIRS;
    private final Queue<Integer> waitingRoom = new LinkedList<>();
    private boolean barberIsAsleep = true;
    private final Condition customerAvailable;
    private final Condition barberAvailable;
    private int currentCustomer = -1;

    public SleepingBarberMonitor(int numOfChairs) {
        this.MAX_NUM_OF_CHAIRS = numOfChairs;
        this.customerAvailable = lock.newCondition();
        this.barberAvailable = lock.newCondition();
    }

    /**
     * Enters the barber shop and places the customer in the waiting room.
     * @param id ID of the customer to be placed in the waiting room.
     */
    public void enterBarberShop(int id) {
        enter(id);
        try {
            if (waitingRoom.size() == MAX_NUM_OF_CHAIRS) {
                System.out.println("Customer " + id + " is leaving (no chairs available)");
                return;
            }

            waitingRoom.add(id);
            System.out.println("Customer " + id + " sits in the waiting room");
            System.out.println("Queue: " + waitingRoom.toString());

            if (barberIsAsleep) {
                barberIsAsleep = false;
                customerAvailable.signal();
            }

            while (currentCustomer != id) {
                barberAvailable.await();
            }

            // Customer is now being served
            System.out.println("Customer " + id + " is getting haircut");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            exit(id);
        }
    }

    /**
     * Retrieves the haircut for the customer with the specified ID.
     * @param id ID of the customer whose haircut is to be retrieved.
     */
    public void getHairCut(int id) {
        enter(id);
        try {
            // Time taken to cut hair
            Thread.sleep(Math.round(Math.random() * 5000) );
            // Haircut is done, notify barber
            barberAvailable.signal();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            exit(id);
        }
    }

    /**
     * Serves the customer currently sitting in the waiting room.
     */
    public void serveCustomer() {
        enter(-1);
        try {
            while (true) {
                if (waitingRoom.isEmpty()) {
                    barberIsAsleep = true;
                    System.out.println("Barber is sleeping...");
                    customerAvailable.await();
                    continue;
                }

                currentCustomer = waitingRoom.remove();
                System.out.println("Barber is cutting hair for customer " + currentCustomer);

                // Signal the specific customer that it's their turn
                barberAvailable.signalAll();

                // Wait for haircut to complete
                barberAvailable.await();

                System.out.println("Barber finished with customer " + currentCustomer);
                currentCustomer = -1;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            exit(-1);
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
