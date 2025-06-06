package com.core.sleepingbarber.gui;

public interface SBBListener {
    void onCustomerLeavesBalking(int id);

    void onCustomerEntersWaitingRoom(int id);

    void onBarberSleeps();

    void onBarberWakesUp();

    void onBarberCallsCustomer(int id);

    void onBarberStartsCutting(int id);

    void onBarberFinishesCutting(int id);

    void onCustomerLeavesSatisfied(int id);
}