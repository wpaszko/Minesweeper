package com.wpaszko.minesweeper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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

    private Model model;


    public void initialize() {
        //model = new Model(Level.EASY);
        model = new Model();

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

    private void addIcon(int columnId, int rowId) {
        Image coveredIcon = new Image("CoveredIcon.jpg");
        Image bombIcon = new Image("BombIcon.jpg");
        Image flagIcon = new Image("FlagIcon.jpg");
        Image zeroIcon = new Image("ZeroIcon.jpg");
        Image oneIcon = new Image("OneIcon.jpg");
        Image twoIcon = new Image("TwoIcon.jpg");
        Image threeIcon = new Image("ThreeIcon.jpg");
        Image fourIcon = new Image("FourIcon.jpg");
        Image fiveIcon = new Image("FiveIcon.jpg");
        Image sixIcon = new Image("SixIcon.jpg");
        Image sevenIcon = new Image("SevenIcon.jpg");
        Image eightIcon = new Image("EightIcon.jpg");
        ImageView square = new ImageView(coveredIcon);
        square.setFitHeight(30);
        square.setFitWidth(30);
        gridPane.add(square, columnId, rowId);

        square.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (model.isCovered(columnId, rowId)) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (model.isBomb(columnId, rowId)) model.uncoverAll(); //Lost game
                        //TODO 1 - wcisnieto bombe
                    else if (model.getBombProperty(columnId, rowId).getValue().equals(BombState.ZERO)) {
                        model.openEmpties(columnId, rowId);
                    }
                    model.uncoverField(columnId, rowId);
                } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                    model.FlagUp(columnId, rowId);
                }
            } else if ((model.isFlagged(columnId, rowId)) && (event.getButton().equals(MouseButton.SECONDARY))) {
                model.FlagDown(columnId, rowId);
            }
        });

        model.getCoverProperty(columnId, rowId).addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(CoverState.UNCOVERED)) {
                switch (model.whichNumber(columnId, rowId)) {
                    case BOMB:
                        square.setImage(bombIcon);
                        break;
                    case ZERO:
                        square.setImage(zeroIcon);
                        break;
                    case ONE:
                        square.setImage(oneIcon);
                        break;
                    case TWO:
                        square.setImage(twoIcon);
                        break;
                    case THREE:
                        square.setImage(threeIcon);
                        break;
                    case FOUR:
                        square.setImage(fourIcon);
                        break;
                    case FIVE:
                        square.setImage(fiveIcon);
                        break;
                    case SIX:
                        square.setImage(sixIcon);
                    case SEVEN:
                        square.setImage(sevenIcon);
                        break;
                    case EIGHT:
                        square.setImage(eightIcon);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + model.whichNumber(columnId, rowId));
                }
            } else if (newValue.equals(CoverState.FLAGGED)) {
                square.setImage(flagIcon);
            } else if (newValue.equals(CoverState.COVERED) && oldValue.equals(CoverState.FLAGGED)) {
                square.setImage(coveredIcon);
            }
        });

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
        Scene scene = null;
        if (pane != null) {
            scene = new Scene(pane);
        }

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Info");
        stage.show();
    }

}
