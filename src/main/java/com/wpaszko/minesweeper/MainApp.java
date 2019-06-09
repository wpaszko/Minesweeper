package com.wpaszko.minesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/MainStackPane.fxml"));

        StackPane stackPane = loader.load();

        Scene scene = new Scene(stackPane);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Saper");
        stage.show();
    }

}

