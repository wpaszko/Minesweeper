package com.wpaszko.minesweeper;

/**
 * Enum określający wartość pola.
 * Do wyboru: puste, bomba, wartość liczbowa od 1 do 8, w zależnosći od ilości bomb naokoło.
 */
public enum BombState {
    BOMB,
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT
}

