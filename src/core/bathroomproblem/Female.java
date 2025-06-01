package core.bathroomproblem;

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
            monitor.enterFemale(id);
            Thread.sleep((int) (Math.random() * 1000));
            monitor.exitFemale(id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
