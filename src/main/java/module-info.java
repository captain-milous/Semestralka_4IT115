module cz.vse.semestralka_4it115 {
    requires javafx.controls;
    requires javafx.fxml;
    requires gson;


    opens cz.vse.semestralka_4it115.main to javafx.fxml;
    exports cz.vse.semestralka_4it115.main;
}