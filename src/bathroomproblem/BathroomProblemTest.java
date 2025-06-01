package bathroomproblem;

public class BathroomProblemTest {
    public static void main(String[] args) {
        BathroomProblemMonitor monitor = new BathroomProblemMonitor();

        for (int i = 0; i < 5; i++) {
            new Male(monitor, i).start();
            new Female(monitor, i).start();
        }
    }
}
