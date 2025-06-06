package com.core.bathroomproblem;

import com.core.bathroomproblem.gui.BathroomStateListener;
import com.core.monitor.Monitor; // Giả sử bạn có lớp này
import java.util.concurrent.locks.Condition;

public class BathroomProblemMonitor extends Monitor {
    private int menInBathroom = 0;
    private int womenInBathroom = 0;
    // Chúng ta không cần các biến waiting này nữa cho logic cơ bản,
    // listener sẽ xử lý trạng thái chờ

    private final Condition canEnter = lock.newCondition();
    private final BathroomStateListener listener;

    // Sửa constructor để nhận listener
    public BathroomProblemMonitor(BathroomStateListener listener) {
        this.listener = listener;
    }

    public void enterMale(int id) {
        enter(id);
        try {
            // Thông báo rằng có một người nam đang chờ
            if (listener != null)
                listener.onMaleWaiting(id);

            while (womenInBathroom > 0) {
                canEnter.await();
            }
            menInBathroom++;

            // Thông báo người nam đã vào
            if (listener != null) {
                listener.onMaleEnter(id);
                listener.logMessage("Man " + id + " entered. Total men: " + menInBathroom);
            }
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

            // Thông báo người nam đã rời đi
            if (listener != null) {
                listener.onMaleExit(id);
                listener.logMessage("Man " + id + " left. Total men: " + menInBathroom);
            }

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
            // Thông báo có một người nữ đang chờ
            if (listener != null)
                listener.onFemaleWaiting(id);

            while (menInBathroom > 0) {
                canEnter.await();
            }
            womenInBathroom++;

            // Thông báo người nữ đã vào
            if (listener != null) {
                listener.onFemaleEnter(id);
                listener.logMessage("Woman " + id + " entered. Total women: " + womenInBathroom);
            }
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

            // Thông báo người nữ đã rời đi
            if (listener != null) {
                listener.onFemaleExit(id);
                listener.logMessage("Woman " + id + " left. Total women: " + womenInBathroom);
            }

            if (womenInBathroom == 0) {
                canEnter.signalAll();
            }
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