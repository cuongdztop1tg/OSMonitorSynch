package com;

import com.view.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        MainMenuController mainMenuController = new MainMenuController();
        fxmlLoader.setController(mainMenuController);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Monitor Synchronization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
