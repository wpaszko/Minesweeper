package com.wpaszko.minesweeper;

public class Model {
    /**
     * Rozkład bomb, pustych pół wraz z numerowanymi polami
     */
    bombState[][] bombLocation;
    /**
     * Rozkład zasłoniętych, odsłoniętych i ofragowanych pół
     */
    coverState[][] coverLocation;
    /**
     * Punkty za czas gry, im krótszy czas, tym więcej punktów
     */
    private int score;
    /**
     * Pozostałe flagi
     */
    private int flags;
    /**
     * Ilośc bomb w grze
     */
    private int bombs;

    private enum bombState {
        BOMB, ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT
    }

    private enum coverState {
        FLAGED, COVERED, UNCOVERED
    }
}
