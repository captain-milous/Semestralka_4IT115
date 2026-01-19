module cz.vse.semestralka_4it115 {
    requires javafx.controls;
    requires javafx.fxml;


    opens cz.vse.semestralka_4it115 to javafx.fxml;
    exports cz.vse.semestralka_4it115;
}