package com.core.bathroomproblem;

public class Female extends Thread {
    private final BathroomProblemMonitor monitor;
    private final int id;

    public Female(BathroomProblemMonitor monitor, int id) {
        this.monitor = monitor;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            // 1. Yêu cầu vào nhà vệ sinh
            monitor.enterFemale(id);

            // 2. GIẢ LẬP THỜI GIAN Ở TRONG NHÀ VỆ SINH (ĐÃ TĂNG LÊN)
            // Ngủ một khoảng thời gian ngẫu nhiên từ 1500ms đến 3000ms (1.5 - 3 giây)
            int timeInside = (int) (Math.random() * 3000) + 6000;
            Thread.sleep(timeInside);

            // 3. Rời khỏi nhà vệ sinh
            monitor.exitFemale(id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}