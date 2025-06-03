package view;

import core.diningphilosopher.DiningPhilosophersMonitor;
import core.diningphilosopher.Philosopher;
import core.diningphilosopher.PhilosopherStateListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class DiningPhilosophersController implements PhilosopherStateListener {
    @FXML
    private Circle philosopher0, philosopher1, philosopher2, philosopher3, philosopher4;

    @FXML
    private Rectangle fork0, fork1, fork2, fork3, fork4;

    @FXML
    private Label state0, state1, state2, state3, state4;

    private Philosopher[] philosophers;
    private DiningPhilosophersMonitor monitor;

    @FXML
    public void initialize() {
        this.monitor = new DiningPhilosophersMonitor(5); // Giả sử bạn đã có
    }

    @FXML
    void btnStartPressed(ActionEvent event) {
        philosophers = new Philosopher[5];
        for (int i = 0; i < 5; i++) {
            philosophers[i] = new Philosopher(i, this, monitor);
            philosophers[i].start();
        }
    }

    @FXML
    void btnStopPressed(ActionEvent event) {
        for (Philosopher p : philosophers) {
            if (p != null) {
                p.stopPhilosopher();
            }
        }
    }

    @FXML
    void btnBackPressed(ActionEvent event) {
        btnStopPressed(event);
         try {
             FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
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

    @Override
    public void onThinking(int id) {
        updateCircle(id, Color.BLUE);
        updateFork(id, Color.BLUE);
        updateState(id, "Thinking");
    }

    @Override
    public void onEating(int id) {
        updateCircle(id, Color.GREEN);
        updateFork(id, Color.GREEN);
        updateState(id, "Eating");
    }

    private void updateCircle(int id, Color color) {
        Platform.runLater(() -> {
            switch (id) {
                case 0 -> philosopher0.setFill(color);
                case 1 -> philosopher1.setFill(color);
                case 2 -> philosopher2.setFill(color);
                case 3 -> philosopher3.setFill(color);
                case 4 -> philosopher4.setFill(color);
            }
        });
    }

    private void updateFork(int id, Color color) {
        Platform.runLater(() -> {
            switch (id) {
                case 0 -> {
                    fork4.setFill(color);
                    fork0.setFill(color);
                }
                case 1 -> {
                    fork0.setFill(color);
                    fork1.setFill(color);
                }
                case 2 -> {
                    fork1.setFill(color);
                    fork2.setFill(color);
                }
                case 3 -> {
                    fork2.setFill(color);
                    fork3.setFill(color);
                }
                case 4 -> {
                    fork3.setFill(color);
                    fork4.setFill(color);
                }
            }
        });
    }

    private void updateState(int id, String state) {
        Platform.runLater(() -> {
            switch (id) {
                case 0 -> state0.setText(state);
                case 1 -> state1.setText(state);
                case 2 -> state2.setText(state);
                case 3 -> state3.setText(state);
                case 4 -> state4.setText(state);
            }
        });
    }
}
