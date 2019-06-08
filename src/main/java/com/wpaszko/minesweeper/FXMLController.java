package com.wpaszko.minesweeper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLController {


    @FXML
    private Button buttonNewGameWin;

    @FXML
    private MenuItem levelChoice;

    @FXML
    private MenuItem newGameChoice;

    @FXML
    private MenuItem menuInfo;

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView viewTimePic;

    @FXML
    private ImageView viewFlagsPic;

    @FXML
    private Text flagsText;

    private Model model;


    public void initialize() {
        model = new Model(Level.EASY, gridPane.getColumnCount(), gridPane.getRowCount());
        flagsText.setText(model.getFlagsValue().toString());
        model.getFlagsProperty().addListener((observable, oldValue, newValue) -> flagsText.setText(newValue.toString()));
        addGraphics();
        addSquares();
    }

    private void addGraphics() {
        Image flagsPic = new Image("FlagsPic.jpg");
        viewFlagsPic.setImage(flagsPic);
    }

    private void addSquares() {
        for (int columnId = 0; columnId < gridPane.getColumnCount(); columnId++) {
            for (int rowId = 0; rowId < gridPane.getRowCount(); rowId++) {
                addIcon(columnId, rowId);
            }
        }
    }

    private void addIcon(int columnId, int rowId) {
        Image coveredIcon = new Image("CoveredIcon.jpg");

        Image flagIcon = new Image("FlagIcon.jpg");

        ImageView square = new ImageView(coveredIcon);
        square.setFitHeight(30);
        square.setFitWidth(30);
        gridPane.add(square, columnId, rowId);
        square.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (model.isCovered(columnId, rowId)) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (model.isBomb(columnId, rowId)) //model.uncoverAll()
                        endGame(false); //Lost game

                    else if (model.getBombProperty(columnId, rowId).getValue().equals(BombState.ZERO)) {
                        model.openEmpties(columnId, rowId);
                    }
                    model.uncoverField(columnId, rowId);
                    System.out.println(model.getCoveredFieldsCounter());
                    System.out.println(model.getCoveredFieldsCounter());
                    if (model.getCoveredFieldsCounter() == model.getBombsValue()) {
                        endGame(true);
                    }
                } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                    model.FlagUp(columnId, rowId);
                    model.flagOnBoard();
                }
            } else if ((model.isFlagged(columnId, rowId)) && (event.getButton().equals(MouseButton.SECONDARY))) {
                model.FlagDown(columnId, rowId);
                model.flagOffBoard();
            }
        });

        model.getCoverProperty(columnId, rowId).addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(CoverState.UNCOVERED)) {
                switch (model.whichNumber(columnId, rowId)) {
                    case BOMB:
                        Image bombIcon = new Image("BombIcon.jpg");
                        square.setImage(bombIcon);
                        break;
                    case ZERO:
                        Image zeroIcon = new Image("ZeroIcon.jpg");
                        square.setImage(zeroIcon);
                        break;
                    case ONE:
                        Image oneIcon = new Image("OneIcon.jpg");
                        square.setImage(oneIcon);
                        break;
                    case TWO:
                        Image twoIcon = new Image("TwoIcon.jpg");
                        square.setImage(twoIcon);
                        break;
                    case THREE:
                        Image threeIcon = new Image("ThreeIcon.jpg");
                        square.setImage(threeIcon);
                        break;
                    case FOUR:
                        Image fourIcon = new Image("FourIcon.jpg");
                        square.setImage(fourIcon);
                        break;
                    case FIVE:
                        Image fiveIcon = new Image("FiveIcon.jpg");
                        square.setImage(fiveIcon);
                        break;
                    case SIX:
                        Image sixIcon = new Image("SixIcon.jpg");
                        square.setImage(sixIcon);
                    case SEVEN:
                        Image sevenIcon = new Image("SevenIcon.jpg");
                        square.setImage(sevenIcon);
                        break;
                    case EIGHT:
                        Image eightIcon = new Image("EightIcon.jpg");
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

    @FXML
    public void endGame(boolean win) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/EndOfGame.fxml"));
        loader.setController(new EndGameController(win));

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

        //stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void newGame() {
        initialize();
    }

    @FXML
    public void newGameMedium() {
        model = new Model(Level.MEDIUM, gridPane.getColumnCount(), gridPane.getRowCount());
        flagsText.setText(model.getFlagsValue().toString());
        model.getFlagsProperty().addListener((observable, oldValue, newValue) -> flagsText.setText(newValue.toString()));
        addSquares();
    }

    @FXML
    public void newGameHard() {
        model = new Model(Level.HARD, gridPane.getColumnCount(), gridPane.getRowCount());
        flagsText.setText(model.getFlagsValue().toString());
        model.getFlagsProperty().addListener((observable, oldValue, newValue) -> flagsText.setText(newValue.toString()));
        addSquares();
    }

}
