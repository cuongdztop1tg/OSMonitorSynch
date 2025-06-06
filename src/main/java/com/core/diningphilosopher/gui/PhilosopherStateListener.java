package com.core.diningphilosopher.gui;

public interface PhilosopherStateListener {
    void onThinking(int id);
    void onEating(int id);
}
