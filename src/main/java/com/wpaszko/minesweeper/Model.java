package com.wpaszko.minesweeper;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.lang.reflect.Array;

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

        bombLocation[5][5].setValue(BombState.BOMB);
        bombLocation[4][4].setValue(BombState.ONE);
        bombLocation[4][5].setValue(BombState.ONE);
        bombLocation[4][6].setValue(BombState.ONE);
        bombLocation[5][4].setValue(BombState.ONE);
        bombLocation[5][6].setValue(BombState.ONE);
        bombLocation[6][4].setValue(BombState.ONE);
        bombLocation[6][5].setValue(BombState.ONE);
        bombLocation[6][6].setValue(BombState.ONE);
    }

    public boolean isBomb(int columnId, int rowId) {
        BombState field = bombLocation[columnId][rowId].getValue();
        return field.equals(BombState.BOMB);
    }

    public BombState whichNumber(int columnId, int rowId) {
        BombState field = bombLocation[columnId][rowId].getValue();
        return field;
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
                coverLocation[columnId][rowId] = new SimpleObjectProperty<>(CoverState.UNCOVERED);
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
