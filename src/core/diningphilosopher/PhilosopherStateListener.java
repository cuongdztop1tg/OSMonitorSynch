package core.diningphilosopher;

public interface PhilosopherStateListener {
    void onThinking(int id);
    void onEating(int id);
}
