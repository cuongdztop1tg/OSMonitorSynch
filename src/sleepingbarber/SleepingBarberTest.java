package sleepingbarber;

public class SleepingBarberTest {
    public static void main(String[] args) {
        int numOfChairs = 3;
        SleepingBarberMonitor monitor = new SleepingBarberMonitor(numOfChairs);
        new Barber(monitor).start();

        for (int id = 0; id < 15; id++) {
            new Customer(id, monitor).start();
            try {
                Thread.sleep(Math.round(Math.random() * 3000)); // Guess comes randomly
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
