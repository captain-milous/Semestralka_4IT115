module cz.vse.semestralka_4it115 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires gson;
    requires java.sql;

    opens cz.vse.semestralka_4it115.main to javafx.fxml;
    exports cz.vse.semestralka_4it115.main;

    // open the precise package that holds your space.Map class:
    opens cz.vse.semestralka_4it115.logic.space to gson;
    opens cz.vse.semestralka_4it115.logic.item to gson;
    opens cz.vse.semestralka_4it115.logic.entity to gson;
}
