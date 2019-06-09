package com.wpaszko.minesweeper;

/**
 * Enum określający możliwe zakrycie pola
 * Do wyboru: oflagowany, zakryty, odkryty.
 */
public enum CoverState {
    /**
     * Pole jest oflagowane, po zdjęciu flagi będzie zakryte.
     */
    FLAGGED,
    /**
     * Pole jest zakryte, można je odkryć, bądź postawić flagę.
     */
    COVERED,
    /**
     * Pole odkryte, nic nie można na nim już wykonać.
     */
    UNCOVERED
}
