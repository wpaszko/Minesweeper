package com.wpaszko.minesweeper;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.lang.reflect.Array;
import java.util.Random;

public class Model {
    /**
     * Rozkład bomb, pustych pół wraz z numerowanymi polami
     */
    private ObjectProperty<BombState>[][] bombLocation = (ObjectProperty<BombState>[][]) Array.newInstance(ObjectProperty.class, 20, 12);
    /**
     * Rozkład zasłoniętych, odsłoniętych i ofragowanych pół
     */
    private ObjectProperty<CoverState>[][] coverLocation = (ObjectProperty<CoverState>[][]) Array.newInstance(ObjectProperty.class, 20, 12);
    /**
     * Punkty za czas gry, im krótszy czas, tym więcej punktów
     */
    private IntegerProperty score;
    /**
     * Pozostałe flagi
     */
    private IntegerProperty flags;
    /**
     * Ilośc bomb w grze
     */
    private IntegerProperty bombs;

    public Model() {

        for (int columnId = 0; columnId < 20; columnId++) {
            for (int rowId = 0; rowId < 12; rowId++) {
                coverLocation[columnId][rowId] = new SimpleObjectProperty<>(CoverState.COVERED);
            }
        }

        for (int columnId = 0; columnId < 20; columnId++) {
            for (int rowId = 0; rowId < 12; rowId++) {
                bombLocation[columnId][rowId] = new SimpleObjectProperty<>(BombState.ZERO);
            }
        }

        createGame();
    }

    /**
     * Funkcja jest wywoływana po to, aby utworzyć plansze, to znaczy rozłożyć bomby i opisac cyframi ich lokacje
     */
    private void createGame() {
        Random rand = new Random();
        int columnId;
        int rowId;
        for (int i = 0; i < 20; i++) {
            columnId = rand.nextInt(19);
            rowId = rand.nextInt(11);
            bombLocation[columnId][rowId].setValue(BombState.BOMB);
        }

        /*
        bombLocation[5][5].setValue(BombState.BOMB);
        bombLocation[4][4].setValue(BombState.ONE);
        bombLocation[4][5].setValue(BombState.ONE);
        bombLocation[4][6].setValue(BombState.ONE);
        bombLocation[5][4].setValue(BombState.ONE);
        bombLocation[5][6].setValue(BombState.ONE);
        bombLocation[6][4].setValue(BombState.ONE);
        bombLocation[6][5].setValue(BombState.ONE);
        bombLocation[6][6].setValue(BombState.ONE);*/

        //bombLocation[1][1].setValue(BombState.BOMB);

        for (columnId = 0; columnId < 20; columnId++) {
            for (rowId = 0; rowId < 12; rowId++) {
                bombLocation[columnId][rowId] = new SimpleObjectProperty<>(howManyBombsAround(columnId, rowId));
                //bombLocation[columnId][rowId] = new SimpleObjectProperty<>(BombState.ZERO);
            }
        }

    }

    /**
     * Funkcja oblicza, ile bomb jest naokoło aktualnego pola
     *
     * @param columnId nr kolumny aktualnego pola
     * @param rowId    nr wiersza aktualnego pola
     * @return Bombstate do ustawienia w bombLocation
     */
    private BombState howManyBombsAround(int columnId, int rowId) {
        BombState result = BombState.BOMB;
        int howMany = 0;
        int i;
        int j;
        if (!bombLocation[columnId][rowId].getValue().equals(BombState.BOMB)) {
            for (i = (columnId - 1); i <= (columnId + 1); i++) {
                if (i < 0 || i > 19) {
                    continue;
                }
                for (j = (rowId - 1); j <= (rowId + 1); j++) {

                    if (j < 0 || j > 11) {
                        continue;
                    }
                    if (!(i == columnId && j == rowId))
                        if (bombLocation[i][j].getValue().equals(BombState.BOMB)) howMany = howMany + 1;
                }
            }

            switch (howMany) {
                case 0:
                    result = BombState.ZERO;
                    break;
                case 1:
                    result = BombState.ONE;
                    break;
                case 2:
                    result = BombState.TWO;
                    break;
                case 3:
                    result = BombState.THREE;
                    break;
                case 4:
                    result = BombState.FOUR;
                    break;
                case 5:
                    result = BombState.FIVE;
                    break;
                case 6:
                    result = BombState.SIX;
                    break;
                case 7:
                    result = BombState.SEVEN;
                    break;
                case 8:
                    result = BombState.EIGHT;
                    break;
            }
        }
        return result;
    }

    public boolean isBomb(int columnId, int rowId) {
        BombState field = bombLocation[columnId][rowId].getValue();
        return field.equals(BombState.BOMB);
    }

    public BombState whichNumber(int columnId, int rowId) {
        return bombLocation[columnId][rowId].getValue();
    }

    public boolean isCovered(int columnId, int rowId) {
        CoverState field = coverLocation[columnId][rowId].getValue();
        return field.equals(CoverState.COVERED);
    }

    public boolean isUncovered(int columnId, int rowId) {
        CoverState field = coverLocation[columnId][rowId].getValue();
        return field.equals(CoverState.UNCOVERED);
    }

    public boolean isFlagged(int columnId, int rowId) {
        CoverState field = coverLocation[columnId][rowId].getValue();
        return field.equals(CoverState.FLAGGED);
    }

    public boolean isFlaggedOrUncovered(int columnId, int rowId) {
        CoverState field = coverLocation[columnId][rowId].getValue();
        return !field.equals(CoverState.UNCOVERED);
    }

    public void setValueOfBomb(int columnId, int rowId, BombState bombState) {
        bombLocation[columnId][rowId].setValue(bombState);
    }

    public void FlagUp(int columnId, int rowId) {
        coverLocation[columnId][rowId].setValue(CoverState.FLAGGED);
    }

    public void FlagDown(int columnId, int rowId) {
        coverLocation[columnId][rowId].setValue(CoverState.COVERED);
    }

    public void uncoverField(int columnId, int rowId) {
        coverLocation[columnId][rowId].setValue(CoverState.UNCOVERED);
    }

    public void uncoverAll() {
        for (int columnId = 0; columnId < 20; columnId++) {
            for (int rowId = 0; rowId < 12; rowId++) {
                coverLocation[columnId][rowId].setValue(CoverState.UNCOVERED);
            }
        }
    }

    public void uncoverBombs() {
        for (int columnId = 0; columnId < 20; columnId++) {
            for (int rowId = 0; rowId < 12; rowId++) {
                if (bombLocation[columnId][rowId].getValue().equals(BombState.BOMB))
                    coverLocation[columnId][rowId].setValue(CoverState.UNCOVERED);
            }
        }
    }

    public CoverState getValueOfCover(int columnId, int rowId) {
        return coverLocation[columnId][rowId].getValue();
    }

    public ObjectProperty<CoverState> getCoverProperty(int columnId, int rowId) {
        return coverLocation[columnId][rowId];
    }

    public ObjectProperty<BombState> getBombProperty(int columnId, int rowId) {
        return bombLocation[columnId][rowId];
    }


}
