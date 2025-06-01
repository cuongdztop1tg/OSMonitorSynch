package core.bathroomproblem;

public class Male extends Thread {
    private final BathroomProblemMonitor monitor;
    private final int id;

    public Male(BathroomProblemMonitor monitor, int id) {
        this.monitor = monitor;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            monitor.enterMale(id);
            Thread.sleep((int)(Math.random() * 1000));
            monitor.exitMale(id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
