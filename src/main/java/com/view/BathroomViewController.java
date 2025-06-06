package com.view;

import com.core.bathroomproblem.BathroomProblemMonitor;
import com.core.bathroomproblem.Female;
import com.core.bathroomproblem.Male;
import com.core.bathroomproblem.gui.BathroomStateListener;

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

public class BathroomViewController implements Initializable, BathroomStateListener {

    // FXML Components
    @FXML
    private Label bathroomStatusLabel;
    @FXML
    private ListView<Integer> peopleInsideListView;
    @FXML
    private ListView<Integer> menWaitingListView;
    @FXML
    private ListView<Integer> womenWaitingListView;
    @FXML
    private TextArea logTextArea;

    // Data lists for the ListViews
    private final ObservableList<Integer> menWaitingList = FXCollections.observableArrayList();
    private final ObservableList<Integer> womenWaitingList = FXCollections.observableArrayList();
    private final ObservableList<Integer> peopleInsideList = FXCollections.observableArrayList();

    private BathroomProblemMonitor monitor;
    private int maleIdCounter = 0;
    private int femaleIdCounter = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the monitor, passing 'this' controller as the listener
        monitor = new BathroomProblemMonitor(this);

        // Bind the observable lists to the ListViews
        menWaitingListView.setItems(menWaitingList);
        womenWaitingListView.setItems(womenWaitingList);
        peopleInsideListView.setItems(peopleInsideList);
    }

    @FXML
    void onAddMale(ActionEvent event) {
        int id = ++maleIdCounter;
        new Male(monitor, id).start();
    }

    @FXML
    void onAddFemale(ActionEvent event) {
        int id = ++femaleIdCounter;
        new Female(monitor, id).start();
    }

    // --- Implementation of BathroomStateListener methods ---
    // All UI updates MUST be wrapped in Platform.runLater

    @Override
    public void onMaleWaiting(int id) {
        Platform.runLater(() -> menWaitingList.add(id));
    }

    @Override
    public void onFemaleWaiting(int id) {
        Platform.runLater(() -> womenWaitingList.add(id));
    }

    @Override
    public void onMaleEnter(int id) {
        Platform.runLater(() -> {
            menWaitingList.remove(Integer.valueOf(id));
            peopleInsideList.add(id);
            bathroomStatusLabel.setText("Men Inside");
        });
    }

    @Override
    public void onMaleExit(int id) {
        Platform.runLater(() -> {
            peopleInsideList.remove(Integer.valueOf(id));
            if (peopleInsideList.isEmpty()) {
                bathroomStatusLabel.setText("Bathroom is Empty");
            }
        });
    }

    @Override
    public void onFemaleEnter(int id) {
        Platform.runLater(() -> {
            womenWaitingList.remove(Integer.valueOf(id));
            peopleInsideList.add(id);
            bathroomStatusLabel.setText("Women Inside");
        });
    }

    @Override
    public void onFemaleExit(int id) {
        Platform.runLater(() -> {
            peopleInsideList.remove(Integer.valueOf(id));
            if (peopleInsideList.isEmpty()) {
                bathroomStatusLabel.setText("Bathroom is Empty");
            }
        });
    }

    @Override
    public void logMessage(String message) {
        Platform.runLater(() -> logTextArea.appendText(message + "\n"));
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
}