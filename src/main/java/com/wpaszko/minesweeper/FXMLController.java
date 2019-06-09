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

/**
 * <code>FXMLController</code> jest kontrolerem
 * odpowiadającym za widok planszy i obsługę zdarzeń,
 * które wpływają na model.
 * Gdy użytkownik działa na widok
 * to kontroler wywołuje odpowiednią metodę powodującą
 * zmianę w modelu.
 */
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
    private ImageView viewFlagsPic;

    @FXML
    private Text flagsText;

    private Model model;

    /**
     * Kontroler zosyaje zainicjalizowany przy użyciu metody initialize,
     * wykorzystuje przy tym metodę pozwalającą na utowrzenie modelu gry.
     */
    public void initialize() {
        createGame(Level.EASY);
    }

    //tworzy obiekt Model, ustawia listenery na odpowiednie obiekty
    private void createGame(Level level) {
        model = new Model(level, gridPane.getColumnCount(), gridPane.getRowCount());
        flagsText.setText(model.getFlagsValue().toString());
        model.getFlagsProperty().addListener((observable, oldValue, newValue) -> flagsText.setText(newValue.toString()));
        addGraphics();
        addSquares();
        gridPane.setDisable(false);
    }

    //wygrywa grafikę pod planszą
    private void addGraphics() {
        Image flagsPic = new Image("FlagsPic.jpg");
        viewFlagsPic.setImage(flagsPic);
    }

    //dodaje pola gry, na które ustawi listenery
    private void addSquares() {
        for (int columnId = 0; columnId < gridPane.getColumnCount(); columnId++) {
            for (int rowId = 0; rowId < gridPane.getRowCount(); rowId++) {
                addIcon(columnId, rowId);
            }
        }
    }

    //wygrywa odpowiednie grafiki do pól, ustawia listenery
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
                    if (model.isBomb(columnId, rowId)) {
                        model.uncoverAll();
                        endGame(false); //Lost game
                    } else if (model.getBombProperty(columnId, rowId).getValue().equals(BombState.ZERO)) {
                        model.openEmpties(columnId, rowId);
                    }
                    model.uncoverField(columnId, rowId);
                    if (model.getCoveredFieldsCounter() == model.getBombsValue()) {
                        endGame(true); //Won game
                        model.uncoverBombs();
                    }
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
    /**
     * Wyświetla okienku z informacją, gdy użytkownik kliknie "O grze" w menu gry.
     */
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
    /**
     * Uruchomi kontroler okienka końca gry i wyświetli okienko
     * Unieruchamia planszę gry, aby można było ją obejrzeć
     */
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

        gridPane.setDisable(true);
    }


    @FXML
    /**
     * Tworzy nową grę, po wyborze "Łatwy" z menu
     */
    public void newGame() {
        createGame(Level.EASY);
    }

    @FXML
    /**
     * Tworzy nową grę po wyborze "Średni" z menu
     */
    public void newGameMedium() {
        createGame(Level.MEDIUM);
    }

    @FXML
    /**
     * Tworzy nową grę po wyborze "Trudny" z menu
     */
    public void newGameHard() {
        createGame(Level.HARD);
    }

}
