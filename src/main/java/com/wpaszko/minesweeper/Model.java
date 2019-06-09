package com.wpaszko.minesweeper;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.lang.reflect.Array;
import java.util.Random;

/**
 * <code>Model</code> to klasa odpowiadająca za przechowywanie stanu planszy.
 * Udostępnia metody pozwalające na zmianę stanu planszy.
 * Zmiany zachodzące w modelu są automatycznie wyświetlane w widoku,
 * który za pomocą listenerów obserwuje model.
 */
public class Model {

    //tablica obserwowanych obiektów, mówiąca o wartości pól (bomba/liczba/puste)
    private ObjectProperty<BombState>[][] bombLocation = (ObjectProperty<BombState>[][]) Array.newInstance(ObjectProperty.class, 20, 12);

    //tablica obserwowanych obiektów, mówiąca o zakryciu pól (zakryte/odkryte/flaga)
    private ObjectProperty<CoverState>[][] coverLocation = (ObjectProperty<CoverState>[][]) Array.newInstance(ObjectProperty.class, 20, 12);

    //obserwowany obiekt, ile flag pozostało do ustawienia.
    private IntegerProperty flags;

    //ile bomb jest w grze = ile flag ma użytkownik.
    private IntegerProperty bombs;

    //licznik ilości zakrytych pól
    private int coveredFieldsCounter;

    //szerokość planszy w polach
    private int width;

    //wysokość planszy w polach
    private int height;

    //Na jakim poziome trudności odbywa się gra
    private Level level;

    /**
     * Konstruktor obiektu Model tworzy planszę do gry, rozstawia bomby i zasłania wszystkie pola.
     * Poza tym, ustala liczbę flag na podstawie ilości bomb w grze.
     *
     * @param lvl       określa poziom gry przy użyciu enum Level
     * @param maxColumn określa szerokość planszy (ile pól będzie w jednym rzędzie)
     * @param maxRow    określa wysokośc planszy (ile pól będzie w jednej kolumnie)
     */
    public Model(Level lvl, int maxColumn, int maxRow) {
        width = maxColumn;
        height = maxRow;
        level = lvl;
        bombs = new SimpleIntegerProperty();
        flags = new SimpleIntegerProperty();
        coveredFieldsCounter = width * height;
        setAmountOfBombs();
        flags.setValue(bombs.getValue());

        coverEverything();

        setAllZeros();

        createGame();
    }

    //W zależności od poziomu gry wybiera odpowiednią ilość bomb
    private void setAmountOfBombs() {
        if (level.equals(Level.EASY)) bombs.set(20);
        else if (level.equals(Level.MEDIUM)) bombs.set(30);
        else if (level.equals(Level.HARD)) bombs.set(40);
    }

    //Tworzy obiekty tablicy wartości pól i ustawia wszystkie pola na puste
    private void setAllZeros() {
        for (int columnId = 0; columnId < width; columnId++) {
            for (int rowId = 0; rowId < height; rowId++) {
                bombLocation[columnId][rowId] = new SimpleObjectProperty<>(BombState.ZERO);
            }
        }
    }

    //Służy do rozstawienia bomb i określenia wartości pół naokoło bomb
    private void createGame() {
        Random rand = new Random();
        int columnId;
        int rowId;

        //Jeśli na danym polu znajduje się juz bomba, to wylosuj nowe pole
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

    //ustawia wartości wszystkich pól na zasłonięte
    private void coverEverything() {
        for (int columnId = 0; columnId < width; columnId++) {
            for (int rowId = 0; rowId < height; rowId++) {
                coverLocation[columnId][rowId] = new SimpleObjectProperty<>(CoverState.COVERED);
            }
        }
    }

    //oblicza wartośc pola na podstawie tego, ile bomb jest naokoło
    private BombState howManyBombsAround(int columnId, int rowId) {
        BombState result = BombState.BOMB;
        if (!bombLocation[columnId][rowId].getValue().equals(BombState.BOMB)) {
            int howMany = 0;
            int i;
            int j;
            //policz ile bomb jest naokoło pola
            for (i = (columnId - 1); i <= (columnId + 1); i++) {
                if (i < 0 || i > width - 1) //warunek brzegowy
                {
                    continue;
                }
                for (j = (rowId - 1); j <= (rowId + 1); j++) {

                    if (j < 0 || j > height - 1) //warunek brzegowy
                    {
                        continue;
                    }
                    if (!(i == columnId && j == rowId)) //jeśli jest bomba, dodaj do sumy
                        if (bombLocation[i][j].getValue().equals(BombState.BOMB)) howMany = howMany + 1;
                }
            }

            switch (howMany) //w zależnosci od sumy ustaw wartość pola
            {
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

    /**
     * Ustala, czy na wskazanym polu jest bomba.
     *
     * @param columnId kolumna wybranego pola
     * @param rowId    rząd wybranego pola
     * @return Prawda, jeśli bomba jest pod wskazanym polem
     */
    public boolean isBomb(int columnId, int rowId) {
        BombState field = bombLocation[columnId][rowId].getValue();
        return field.equals(BombState.BOMB);
    }

    /**
     * Ustala, jaka wartosc(puste, liczba, czy bomba) jest pod wybranym polem.
     *
     * @param columnId kolumna wybranego pola
     * @param rowId    rząd wybranego pola
     * @return wartość wybranego pola
     */
    public BombState whichNumber(int columnId, int rowId) {
        return bombLocation[columnId][rowId].getValue();
    }

    /**
     * Sprawdza, czy dane pole jest zakryte.
     *
     * @param columnId kolumna wybranego pola
     * @param rowId    rząd wybranego pola
     * @return Prawda, jeśli pole jest zakryte.
     */
    public boolean isCovered(int columnId, int rowId) {
        CoverState field = coverLocation[columnId][rowId].getValue();
        return field.equals(CoverState.COVERED);
    }

    /**
     * Sprawdza, czy dane pole jest zaflagowane.
     *
     * @param columnId kolumna wybranego pola
     * @param rowId    rząd wybranego pola
     * @return Prawda, jeśli pole jest zaflagowane
     */
    public boolean isFlagged(int columnId, int rowId) {
        CoverState field = coverLocation[columnId][rowId].getValue();
        return field.equals(CoverState.FLAGGED);
    }

    /**
     * Informuje o ilości bomb na planszy.
     * @return Ilość bomb
     */
    public int getBombsValue() {
        return bombs.getValue();
    }

    /**
     * Ustawia flagę na wybranym polu.
     * Na poczatek sprawdza się, czy jest możliwe postawienie flagi (czy pozostały nieużyte flagi).
     * Pole jest zakrywane flagą, a liczba nieużytych flag zmiejsza się.
     *
     * @param columnId kolumna wybranego pola
     * @param rowId    rząd wybranego pola
     */
    public void FlagUp(int columnId, int rowId) {
        if (flags.getValue() > 0) {
            coverLocation[columnId][rowId].setValue(CoverState.FLAGGED);
            flags.setValue(flags.getValue() - 1);
        }
    }

    /**
     * Zdejmuje flagę z wybranego pola, zwiększąjac przy tym liczbę nieużywanych flag.
     * @param columnId kolumna wybranego pola
     * @param rowId rząd wybranego pola
     */
    public void FlagDown(int columnId, int rowId) {
        coverLocation[columnId][rowId].setValue(CoverState.COVERED);
        flags.set(flags.getValue() + 1);
    }

    /**
     * Odkrywa wskazane pole, zmniejszając przy tym liczbę zakrytych pól.
     * @param columnId kolumna wybranego pola
     * @param rowId rząd wybranego pola
     */
    public void uncoverField(int columnId, int rowId) {
        coverLocation[columnId][rowId].setValue(CoverState.UNCOVERED);
        coveredFieldsCounter = coveredFieldsCounter - 1;
    }

    /**
     * Odkrywa wszystkie pola na planszy, łącznie z zaflagowanymi.
     * Używane w wypadku przegranej gry.
     */
    public void uncoverAll() {
        for (int columnId = 0; columnId < 20; columnId++) {
            for (int rowId = 0; rowId < 12; rowId++) {
                coverLocation[columnId][rowId].setValue(CoverState.UNCOVERED);
            }
        }
    }

    /**
     * Odkrywa planszę poza polami oflagowanymi.
     * Używane w przypadku wygranej.
     */
    public void uncoverBombs() {
        for (int columnId = 0; columnId < 20; columnId++) {
            for (int rowId = 0; rowId < 12; rowId++) {
                if (bombLocation[columnId][rowId].getValue().equals(BombState.BOMB) && coverLocation[columnId][rowId].getValue().equals(CoverState.COVERED))
                    coverLocation[columnId][rowId].setValue(CoverState.UNCOVERED);
            }
        }
    }

    /**
     * Zwraca CoverProperty, który jest obiektem mówiącym o zakryciu pola (zakryte/odkryte/zaflagowane).
     *
     * @param columnId kolumna wybranego pola
     * @param rowId    rząd wybranego pola
     * @return wskazanie na wybrany obiekt CoverProperty
     */
    public ObjectProperty<CoverState> getCoverProperty(int columnId, int rowId) {
        return coverLocation[columnId][rowId];
    }

    /**
     * Zwraca BombProperty, który jest obiektem mówiącym o wartości pola (puste/liczba/bomba).
     *
     * @param columnId kolumna wybranego pola
     * @param rowId    rząd wybranego pola
     * @return wskazanie na wybrany obiekt BombProperty
     */
    public ObjectProperty<BombState> getBombProperty(int columnId, int rowId) {
        return bombLocation[columnId][rowId];
    }


    /**
     * Rekurencyjna funkcja, otwierająca puste pola i ich liczbowych sąsiadów.
     * Uzywana, gdy użytkownik kliknie w puste pole.
     * @param columnId kolumna wybranego pola
     * @param rowId rząd wybranego pola
     */
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


    /**
     * Zwraca obiekt flags, na który zostanie nałożony listener.
     * Dzięki temu użytkownik wie, ile flag mu pozostało.
     * @return Obiekt IntegerProperty.
     */
    public IntegerProperty getFlagsProperty() {
        return flags;
    }

    /**
     * Zwraca liczbę, ile flag pozostało do użytku użytkownika.
     * @return Integer, ile flag pozostało do wykorzystania.
     */
    public Integer getFlagsValue() {
        return flags.getValue();
    }

    /**
     * Zwraca ilość zakrytych pól.
     * Jeśli liczba zakrytych pól = ilośc bomb w grze to wygrana.
     * @return Integer, ile pól jest zakrytych.
     */
    public Integer getCoveredFieldsCounter() {
        return coveredFieldsCounter;
    }
}
