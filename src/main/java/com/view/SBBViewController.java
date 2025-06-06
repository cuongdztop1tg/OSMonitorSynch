package com.view;

import com.core.sleepingbarber.Barber;
import com.core.sleepingbarber.Customer;
import com.core.sleepingbarber.SleepingBarberMonitor;
import com.core.sleepingbarber.gui.SBBListener;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SBBViewController implements Initializable, SBBListener {

    private static final int NUM_OF_CHAIRS = 3;

    @FXML
    private Label barberStatusLabel;
    @FXML
    private Label customerInChairLabel;
    @FXML
    private Label waitingRoomLabel;
    @FXML
    private ListView<Integer> waitingRoomListView;
    @FXML
    private TextArea logTextArea;

    private final ObservableList<Integer> waitingRoomList = FXCollections.observableArrayList();
    private SleepingBarberMonitor monitor;
    private int customerIdCounter = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monitor = new SleepingBarberMonitor(NUM_OF_CHAIRS, this);
        waitingRoomListView.setItems(waitingRoomList);
        updateWaitingRoomLabel();

        // Create and start the single barber thread
        Barber barber = new Barber(monitor);
        barber.setDaemon(true); // Set as daemon so it exits with the app
        barber.start();
    }

    @FXML
    void onAddCustomer() {
        int id = ++customerIdCounter;
        logMessage("[EVENT] A new customer (ID: " + id + ") is approaching the shop.");
        Customer customer = new Customer(id, monitor);
        customer.setDaemon(true);
        customer.start();
    }

    @FXML
    void onStop(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/MainMenu.fxml"));
            MainMenuController controller = new MainMenuController();
            fxmlLoader.setController(controller);
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateWaitingRoomLabel() {
        waitingRoomLabel.setText(String.format("Waiting Room (%d/%d)", waitingRoomList.size(), NUM_OF_CHAIRS));
    }

    private void logMessage(String message) {
        Platform.runLater(() -> logTextArea.appendText(message + "\n"));
    }

    // --- Implementation of SBBListener methods ---
    // All UI updates MUST be wrapped in Platform.runLater

    @Override
    public void onCustomerLeavesBalking(int id) {
        Platform.runLater(() -> logMessage("--- Customer " + id + " left because the shop was full."));
    }

    @Override
    public void onCustomerEntersWaitingRoom(int id) {
        Platform.runLater(() -> {
            logMessage("Customer " + id + " sat in the waiting room.");
            waitingRoomList.add(id);
            updateWaitingRoomLabel();
        });
    }

    @Override
    public void onBarberSleeps() {
        Platform.runLater(() -> {
            logMessage("Barber is sleeping... Zzz...");
            barberStatusLabel.setText("Sleeping");
        });
    }

    @Override
    public void onBarberWakesUp() {
        Platform.runLater(() -> logMessage("Customer woke up the barber!"));
    }

    @Override
    public void onBarberCallsCustomer(int id) {
        Platform.runLater(() -> {
            logMessage("Barber called Customer " + id + " to the chair.");
            waitingRoomList.remove(Integer.valueOf(id));
            updateWaitingRoomLabel();
            customerInChairLabel.setText("Customer " + id);
        });
    }

    @Override
    public void onBarberStartsCutting(int id) {
        Platform.runLater(() -> {
            logMessage(">>> Barber started cutting hair for Customer " + id);
            barberStatusLabel.setText("Cutting Hair");
        });
    }

    @Override
    public void onBarberFinishesCutting(int id) {
        Platform.runLater(() -> {
            logMessage("<<< Barber finished with Customer " + id);
            barberStatusLabel.setText("Idle");
            customerInChairLabel.setText("EMPTY");
        });
    }

    @Override
    public void onCustomerLeavesSatisfied(int id) {
        Platform.runLater(() -> logMessage("+++ Customer " + id + " left the shop happily."));
    }
}