package readerwriter;

public class Writer extends Thread {
    private ReaderWriterMonitor monitor;
    private int id;

    public Writer(int id, ReaderWriterMonitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                monitor.startWriting(id);
                System.out.println("Writer " + id + " started writing");
                Thread.sleep(Math.round(Math.random() * 5000));
                monitor.stopWriting(id);
                System.out.println("Writer " + id + " stopped writing");
                Thread.sleep(Math.round(Math.random() * 5000));
            }
        } catch (InterruptedException e) {
            System.out.println("Writer " + id + " interrupted");
        }
    }
}
