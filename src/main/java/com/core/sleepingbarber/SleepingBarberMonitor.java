package com.core.sleepingbarber;

import com.core.monitor.Monitor;
import com.core.sleepingbarber.gui.SBBListener; // Import listener
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;

public class SleepingBarberMonitor extends Monitor {
    private final int MAX_NUM_OF_CHAIRS;
    private final Queue<Integer> waitingRoom = new LinkedList<>();
    private final Condition barberSleeps = lock.newCondition();
    private final Condition customerWaits = lock.newCondition();
    private boolean isBarberSleeping = true;
    private int customerInChair = -1;
    private boolean haircutFinished = false;
    private final SBBListener listener; // Thêm listener

    // Sửa constructor
    public SleepingBarberMonitor(int numOfChairs, SBBListener listener) {
        this.MAX_NUM_OF_CHAIRS = numOfChairs;
        this.listener = listener;
    }

    public boolean requestHaircut(int id) {
        enter(id);
        try {
            if (waitingRoom.size() == MAX_NUM_OF_CHAIRS) {
                if (listener != null)
                    listener.onCustomerLeavesBalking(id);
                return false;
            }

            waitingRoom.add(id);
            if (listener != null)
                listener.onCustomerEntersWaitingRoom(id);

            if (isBarberSleeping) {
                if (listener != null)
                    listener.onBarberWakesUp();
                barberSleeps.signal();
            }

            while (true) {
                customerWaits.await();
                if (customerInChair == id && haircutFinished) {
                    break;
                }
            }
            if (listener != null)
                listener.onCustomerLeavesSatisfied(id);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            exit(id);
        }
    }

    public void cutHair() {
        enter(-1);
        try {
            while (waitingRoom.isEmpty()) {
                if (listener != null)
                    listener.onBarberSleeps();
                isBarberSleeping = true;
                barberSleeps.await();
                isBarberSleeping = false;
            }

            customerInChair = waitingRoom.poll();
            if (listener != null)
                listener.onBarberCallsCustomer(customerInChair);
            haircutFinished = false;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            exit(-1);
        }

        try {
            if (listener != null)
                listener.onBarberStartsCutting(customerInChair);
            Thread.sleep(Math.round(Math.random() * 4000) + 4000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        enter(-1);
        try {
            haircutFinished = true;
            if (listener != null)
                listener.onBarberFinishesCutting(customerInChair);
            customerWaits.signalAll();
            customerInChair = -1;
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