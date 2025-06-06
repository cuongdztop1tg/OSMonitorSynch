package com.core.bathroomproblem.gui;

public interface BathroomStateListener {
    void onMaleWaiting(int id);

    void onFemaleWaiting(int id);

    void onMaleEnter(int id);

    void onMaleExit(int id);

    void onFemaleEnter(int id);

    void onFemaleExit(int id);

    void logMessage(String message);
}