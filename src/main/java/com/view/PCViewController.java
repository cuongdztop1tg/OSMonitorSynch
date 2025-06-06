package com.view;

import com.core.producerconsumer.Consumer;
import com.core.producerconsumer.Producer;
import com.core.producerconsumer.ProducerConsumerMonitor;
import com.core.producerconsumer.gui.ProducerConsumerListener;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PCViewController implements Initializable, ProducerConsumerListener<Integer> {

    private static final int BUFFER_SIZE = 10;

    @FXML
    private Button addProducerButton;
    @FXML
    private Button addConsumerButton;
    @FXML
    private Button stopButton;

    @FXML
    private ListView<String> producersListView;
    @FXML
    private ListView<String> consumersListView;
    @FXML
    private ListView<Integer> bufferListView;
    @FXML
    private Label bufferStatusLabel;
    @FXML
    private TextArea logTextArea;

    private final ObservableList<String> producersList = FXCollections.observableArrayList();
    private final ObservableList<String> consumersList = FXCollections.observableArrayList();
    private final ObservableList<Integer> bufferList = FXCollections.observableArrayList();

    private final List<Thread> activeThreads = new ArrayList<>();

    private ProducerConsumerMonitor<Integer> monitor;
    private int producerIdCounter = 0;
    private int consumerIdCounter = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monitor = new ProducerConsumerMonitor<>(BUFFER_SIZE, this);
        producersListView.setItems(producersList);
        consumersListView.setItems(consumersList);
        bufferListView.setItems(bufferList);
        updateBufferStatusLabel();
    }

    @FXML
    void onAddProducer() {
        int id = ++producerIdCounter;
        producersList.add("Producer " + id + " (Producing)");
        Thread producerThread = new Producer(id, monitor);
        activeThreads.add(producerThread); // Thêm vào danh sách theo dõi
        producerThread.start();
    }

    @FXML
    void onAddConsumer() {
        int id = ++consumerIdCounter;
        consumersList.add("Consumer " + id + " (Active)");
        Thread consumerThread = new Consumer(id, monitor, this);
        activeThreads.add(consumerThread); // Thêm vào danh sách theo dõi
        consumerThread.start();
    }

    // Phương thức xử lý cho nút Stop
    @FXML
    void onStop(ActionEvent event) throws IOException {
        logMessage("--- STOPPING ALL THREADS ---");

        // Ngắt tất cả các luồng đang hoạt động
        for (Thread t : activeThreads) {
            t.interrupt();
        }

        // Vô hiệu hóa các nút để ngăn tạo thêm luồng
        addProducerButton.setDisable(true);
        addConsumerButton.setDisable(true);
        stopButton.setDisable(true);

        activeThreads.clear();

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

    private void updateBufferStatusLabel() {
        bufferStatusLabel.setText(String.format("Shared Buffer (%d/%d)", bufferList.size(), BUFFER_SIZE));
    }

    private void updateStatusInList(ObservableList<String> list, int id, String prefix, String status) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).startsWith(prefix + " " + id)) {
                list.set(i, String.format("%s %d (%s)", prefix, id, status));
                return;
            }
        }
    }

    // --- Implementation of ProducerConsumerListener methods ---
    // All UI updates MUST be wrapped in Platform.runLater
    @Override
    public void onProducerWaiting(int id) {
        Platform.runLater(() -> updateStatusInList(producersList, id, "Producer", "Waiting - Buffer Full"));
    }

    @Override
    public void onProducerResumed(int id) {
        Platform.runLater(() -> updateStatusInList(producersList, id, "Producer", "Producing"));
    }

    @Override
    public void onConsumerWaiting(int id) {
        Platform.runLater(() -> updateStatusInList(consumersList, id, "Consumer", "Waiting - Buffer Empty"));
    }

    @Override
    public void onConsumerResumed(int id) {
        Platform.runLater(() -> updateStatusInList(consumersList, id, "Consumer", "Active"));
    }

    @Override
    public void onProduce(int producerId, Integer item) {
        Platform.runLater(() -> {
            bufferList.add(item);
            updateBufferStatusLabel();
        });
    }

    @Override
    public void onConsume(int consumerId, Integer item) {
        Platform.runLater(() -> {
            bufferList.remove(item);
            updateStatusInList(consumersList, consumerId, "Consumer", "Consuming item " + item);
            updateBufferStatusLabel();
        });
    }

    @Override
    public void logMessage(String message) {
        Platform.runLater(() -> logTextArea.appendText(message + "\n"));
    }
}