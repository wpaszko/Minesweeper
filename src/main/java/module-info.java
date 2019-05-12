module minesweeper {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.wpaszko.minesweeper to javafx.fxml;
    exports com.wpaszko.minesweeper;
}