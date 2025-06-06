package com.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {

    @FXML
    void btnBathroomProblemPressed(ActionEvent event) { loadScene("/com/BathroomProblem.fxml", event); }

    @FXML
    void btnDiningPhilosopherPressed(ActionEvent event) {
        loadScene("/com/DiningPhilosophers.fxml", event);
    }

    @FXML
    void btnProducerConsumerPressed(ActionEvent event) {
        loadScene("/com/ProducerConsumer.fxml", event);
    }

    @FXML
    void btnReadersWritersPressed(ActionEvent event) {
        loadScene("/com/ReadersWriters.fxml", event);
    }

    @FXML
    void btnSleepingBarberPressed(ActionEvent event) {
        loadScene("/com/SleepingBarber.fxml", event);
    }

    private void loadScene(String fxmlFile, ActionEvent event) {
        try {
            // Load new fxml file
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create new scene with the root and set the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
