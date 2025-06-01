package core.readerwriter;

public class ReaderWriterTest {
    public static void main(String[] args) {
        ReaderWriterMonitor monitor = new ReaderWriterMonitor();
        for (int id = 0; id < 3; id++) {
            new Reader(id, monitor).start();
        }

        for (int id = 0; id < 2; id++) {
            new Writer(id, monitor).start();
        }
    }
}
