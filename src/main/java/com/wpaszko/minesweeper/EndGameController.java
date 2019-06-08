package com.wpaszko.minesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EndGameController {

    @FXML
    private Text okText;

    private boolean win;

    public EndGameController(boolean win) {
        this.win = win;
    }

    @FXML
    private void initialize() {
        if (win)
            okText.setText("Wygrana");
        else
            okText.setText("Porażka");
    }

    @FXML
    void closeWindow(MouseEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }


}
