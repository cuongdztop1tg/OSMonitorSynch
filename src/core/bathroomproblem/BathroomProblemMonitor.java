package core.bathroomproblem;

import core.monitor.Monitor;

import java.util.concurrent.locks.Condition;

public class BathroomProblemMonitor extends Monitor {
    private int menInBathroom = 0;
    private int womenInBathroom = 0;
    private boolean menWaiting = false;
    private boolean womenWaiting = false;

    private final Condition canEnter = lock.newCondition();

    public void enterMale(int id) {
        enter(id);
        try {
            menWaiting = true;
            while (womenInBathroom > 0) {
                canEnter.await();
            }
            menWaiting = false;
            menInBathroom++;
            System.out.println("Man " + id + " entered the bathroom. Total men: " + menInBathroom);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            exit(id);
        }
    }

    public void exitMale(int id) {
        enter(id);
        try {
            menInBathroom--;
            System.out.println("Man " + id + " left the bathroom. Total men: " + menInBathroom);
            if (menInBathroom == 0) {
                canEnter.signalAll();
            }
        } finally {
            exit(id);
        }
    }

    public void enterFemale(int id) {
        enter(id);
        try {
            womenWaiting = true;
            while (menInBathroom > 0) {
                canEnter.await();
            }
            womenWaiting = false;
            womenInBathroom++;
            System.out.println("Woman " + id + " entered the bathroom. Total women: " + womenInBathroom);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            exit(id);
        }
    }

    public void exitFemale(int id) {
        enter(id);
        try {
            womenInBathroom--;
            System.out.println("Woman " + id + " left the bathroom. Total women: " + womenInBathroom);
            if (womenInBathroom == 0) {
                canEnter.signalAll();
            }
        } finally {
            exit(id);
        }
    }

    // Optional: Use abstract methods if you want a unified access point (not required)
    @Override
    public void enter(int id) {
        lock.lock();
    }

    @Override
    public void exit(int id) {
        lock.unlock();
    }
}
