package com.wpaszko.minesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Kontroler okienka końca gry
 */
public class EndGameController {

    @FXML
    private Text okText;

    @FXML
    private ImageView endPicView;

    private boolean win;

    /**
     * Służy do ustalenia, czy gra zakończyła się wygraną, czy porażką użytkownika
     *
     * @param win Prawda, jeśli użytkownik wygrał grę.
     */
    public EndGameController(boolean win) {
        this.win = win;
    }

    @FXML
    private void initialize() {
        if (win) {
            okText.setText("Wygrana!");
            Image flagsPic = new Image("winPic.jpg");
            endPicView.setImage(flagsPic);
        } else {
            okText.setText("Porażka!");
            Image flagsPic = new Image("losePic.jpg");
            endPicView.setImage(flagsPic);
        }
    }

    @FXML
    void closeWindow(MouseEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }


}
