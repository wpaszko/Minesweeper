package com.wpaszko.minesweeper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLController {

    @FXML
    private MenuItem menuInfo;

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView viewTimePic;

    @FXML
    private TextArea textAreaTime;

    @FXML
    private ImageView viewFlagsPic;

    @FXML
    private TextArea textAreaFlags;

    public void initialize() {

        addGraphics();

        for (int columnId = 0; columnId < 20; columnId++) {
            for (int rowId = 0; rowId < 12; rowId++) {
                addIcon(columnId, rowId);
            }
        }
    }

    private void addGraphics() {
        Image timePic = new Image("ClockPic.jpg");
        Image flagsPic = new Image("FlagsPic.jpg");
        viewTimePic.setImage(timePic);
        viewFlagsPic.setImage(flagsPic);
    }

    @FXML
    public void openInfoWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/AboutGame.fxml"));

        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        Scene scene = new Scene(pane);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Info");
        stage.show();
    }


    private void addIcon(int columnId, int rowId) {
        Image coveredIcon = new Image("CoveredIcon.jpg");
        ImageView square = new ImageView(coveredIcon);
        square.setFitHeight(30);
        square.setFitWidth(30);
        gridPane.add(square, columnId, rowId);
    }
}
