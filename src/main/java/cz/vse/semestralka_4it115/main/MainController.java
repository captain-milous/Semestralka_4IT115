package cz.vse.semestralka_4it115.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainController {
    @FXML
    private Label playerName;
    @FXML
    private Label playerHealth;
    @FXML
    private Label playerStrength;
    @FXML
    private Label playerDefence;
    @FXML
    private Label playerMoney;
    @FXML
    private TextField vstup;

    public void odesliVstup(ActionEvent actionEvent) {
        System.out.println(vstup.getText());
        System.out.println(playerName.getText());
    }
}
