package readerwriter;

public class Reader extends Thread {
    private ReaderWriterMonitor monitor;
    private int id;

    public Reader(int id, ReaderWriterMonitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                monitor.startReading(id);
                System.out.println("Reader " + id + " started reading");
                Thread.sleep(Math.round(Math.random() * 5000));
                monitor.stopReading(id);
                System.out.println("Reader " + id + " stopped reading");
                Thread.sleep(Math.round(Math.random() * 5000));
            }
        } catch (InterruptedException e) {
            System.out.println("Reader " + id + " interrupted");
        }
    }
}
