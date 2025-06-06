package com.view;

import com.core.readerwriter.Reader;
import com.core.readerwriter.ReaderWriterMonitor;
import com.core.readerwriter.Writer;
import com.core.readerwriter.gui.RWListener;

import javafx.event.ActionEvent;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RWViewController implements Initializable, RWListener {
    @FXML
    private Button addReaderButton;
    @FXML
    private Button addWriterButton;
    @FXML
    private Button stopButton;

    @FXML
    private ListView<Integer> readersWaitingListView;
    @FXML
    private ListView<Integer> writersWaitingListView;
    @FXML
    private ListView<Integer> activeReadersListView;
    @FXML
    private Label resourceStatusLabel;
    @FXML
    private TextArea logTextArea;

    private final ObservableList<Integer> readersWaitingList = FXCollections.observableArrayList();
    private final ObservableList<Integer> writersWaitingList = FXCollections.observableArrayList();
    private final ObservableList<Integer> activeReadersList = FXCollections.observableArrayList();

    private final List<Thread> activeThreads = new ArrayList<>();

    private ReaderWriterMonitor monitor;
    private int readerIdCounter = 0;
    private int writerIdCounter = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monitor = new ReaderWriterMonitor(this);
        readersWaitingListView.setItems(readersWaitingList);
        writersWaitingListView.setItems(writersWaitingList);
        activeReadersListView.setItems(activeReadersList);
    }

    @FXML
    void onAddReader() {
        int id = ++readerIdCounter;
        Thread readerThread = new Reader(id, monitor);
        activeThreads.add(readerThread); // Thêm luồng vào danh sách theo dõi
        readerThread.start();
    }

    @FXML
    void onAddWriter() {
        int id = ++writerIdCounter;
        Thread writerThread = new Writer(id, monitor);
        activeThreads.add(writerThread); // Thêm luồng vào danh sách theo dõi
        writerThread.start();
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
        addReaderButton.setDisable(true);
        addWriterButton.setDisable(true);
        stopButton.setDisable(true);

        // Xóa danh sách các luồng
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

    private void updateResourceStatus() {
        if (!activeReadersList.isEmpty()) {
            resourceStatusLabel.setText("READERS ACTIVE");
            resourceStatusLabel.setTextFill(Color.BLUE);
        } else if (resourceStatusLabel.getText().contains("WRITING")) {
            // Do nothing, a writer is active
        } else {
            resourceStatusLabel.setText("EMPTY");
            resourceStatusLabel.setTextFill(Color.GREEN);
        }
    }

    // --- Implementation of RWListener methods ---
    // All UI updates MUST be wrapped in Platform.runLater

    @Override
    public void onReaderWaiting(int id) {
        Platform.runLater(() -> {
            logMessage("Reader " + id + " is waiting.");
            readersWaitingList.add(id);
        });
    }

    @Override
    public void onWriterWaiting(int id) {
        Platform.runLater(() -> {
            logMessage("Writer " + id + " is waiting.");
            writersWaitingList.add(id);
        });
    }

    @Override
    public void onReaderStart(int id) {
        Platform.runLater(() -> {
            logMessage("Reader " + id + " started reading.");
            readersWaitingList.remove(Integer.valueOf(id));
            activeReadersList.add(id);
            updateResourceStatus();
        });
    }

    @Override
    public void onReaderStop(int id) {
        Platform.runLater(() -> {
            logMessage("Reader " + id + " stopped reading.");
            activeReadersList.remove(Integer.valueOf(id));
            updateResourceStatus();
        });
    }

    @Override
    public void onWriterStart(int id) {
        Platform.runLater(() -> {
            logMessage("Writer " + id + " started writing.");
            writersWaitingList.remove(Integer.valueOf(id));
            resourceStatusLabel.setText("WRITER " + id + " IS WRITING");
            resourceStatusLabel.setTextFill(Color.RED);
        });
    }

    @Override
    public void onWriterStop(int id) {
        Platform.runLater(() -> {
            logMessage("Writer " + id + " stopped writing.");
            updateResourceStatus();
        });
    }

    @Override
    public void logMessage(String message) {
        Platform.runLater(() -> logTextArea.appendText(message + "\n"));
    }
}