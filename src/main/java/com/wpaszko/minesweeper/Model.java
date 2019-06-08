package com.wpaszko.minesweeper;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
     * Pozostałe flagi
     */
    private IntegerProperty flags;
    /**
     * Ilośc bomb w grze
     */
    private IntegerProperty bombs;

    private int coveredFieldsCounter;

    private int width;
    private int height;

    private Level level;

    public Model(Level lvl, int maxColumn, int maxRow) {
        width = maxColumn;
        height = maxRow;
        level = lvl;
        bombs = new SimpleIntegerProperty();
        flags = new SimpleIntegerProperty();
        coveredFieldsCounter = width * height;
        if (level.equals(Level.EASY)) bombs.set(25);
        else if (level.equals(Level.MEDIUM)) bombs.set(30);
        else if (level.equals(Level.HARD)) bombs.set(40);
        flags.setValue(bombs.getValue());

        coverEverything();

        setAllZeros();

        createGame();
    }

    private void setAllZeros() {
        for (int columnId = 0; columnId < width; columnId++) {
            for (int rowId = 0; rowId < height; rowId++) {
                bombLocation[columnId][rowId] = new SimpleObjectProperty<>(BombState.ZERO);
            }
        }
    }

    /**
     * Funkcja jest wywoływana po to, aby utworzyć plansze, to znaczy rozłożyć bomby i opisac cyframi ich lokacje
     */
    private void createGame() {
        Random rand = new Random();
        int columnId;
        int rowId;

        for (int i = 0; i < bombs.get(); i++) {
            do {
                columnId = rand.nextInt(19);
                rowId = rand.nextInt(11);
            } while ((bombLocation[columnId][rowId].getValue().equals(BombState.BOMB)));

            bombLocation[columnId][rowId].setValue(BombState.BOMB);
        }


        for (columnId = 0; columnId < width; columnId++) {
            for (rowId = 0; rowId < height; rowId++) {
                bombLocation[columnId][rowId] = new SimpleObjectProperty<>(howManyBombsAround(columnId, rowId));
            }
        }
    }

    private void coverEverything() {
        for (int columnId = 0; columnId < width; columnId++) {
            for (int rowId = 0; rowId < height; rowId++) {
                coverLocation[columnId][rowId] = new SimpleObjectProperty<>(CoverState.COVERED);
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
                if (i < 0 || i > width - 1) {
                    continue;
                }
                for (j = (rowId - 1); j <= (rowId + 1); j++) {

                    if (j < 0 || j > height - 1) {
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

    public int getBombsValue() {
        return bombs.getValue();
    }

    public void FlagUp(int columnId, int rowId) {
        coverLocation[columnId][rowId].setValue(CoverState.FLAGGED);
    }

    public void FlagDown(int columnId, int rowId) {
        coverLocation[columnId][rowId].setValue(CoverState.COVERED);
    }

    public void uncoverField(int columnId, int rowId) {
        coverLocation[columnId][rowId].setValue(CoverState.UNCOVERED);
        coveredFieldsCounter = coveredFieldsCounter - 1;
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


    public void openEmpties(int columnId, int rowId) {
        int i;
        int j;
        coverLocation[columnId][rowId].setValue(CoverState.UNCOVERED);

        for (i = (columnId - 1); i <= (columnId + 1); i++) {
            if (i < 0 || i > 19) {
                continue;
            }
            for (j = (rowId - 1); j <= (rowId + 1); j++) {

                if (j < 0 || j > 11) {
                    continue;
                }
                if (!(i == columnId && j == rowId)) {
                    if (coverLocation[i][j].getValue().equals(CoverState.COVERED)) {
                        if (bombLocation[i][j].getValue().equals(BombState.ZERO)) {
                            openEmpties(i, j);
                            coveredFieldsCounter = coveredFieldsCounter - 1;
                        } else if (!(bombLocation[i][j].getValue().equals(BombState.BOMB))) {
                            uncoverField(i, j);
                        }
                    }
                }
            }

        }
    }

    public void flagOnBoard() {
        flags.set(flags.getValue() - 1);
    }

    public void flagOffBoard() {
        flags.set(flags.getValue() + 1);
    }

    public IntegerProperty getFlagsProperty() {
        return flags;
    }

    public Integer getFlagsValue() {
        return flags.getValue();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Integer getCoveredFieldsCounter() {
        return coveredFieldsCounter;
    }

    public void subCoveredFieldCounter() {
        coveredFieldsCounter -= 1;
    }
}
