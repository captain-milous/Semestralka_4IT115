package cz.vse.semestralka_4it115.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for player-name dialog.
 */
public class PlayerNameController {
    @FXML
    private TextField nameField;
    @FXML
    private Label validationLabel;
    @FXML
    private Button confirmButton;

    private Stage stage;
    private String resultName;

    @FXML
    private void initialize() {
        validationLabel.setText("Jmeno musi mit 1-14 znaku.");
        confirmButton.setDisable(true);
        nameField.textProperty().addListener((obs, oldVal, newVal) -> updateValidation(newVal));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getResultOrDefault() {
        return resultName == null ? "Hrac" : resultName;
    }

    @FXML
    private void onConfirm() {
        String candidate = normalize(nameField.getText());
        if (!isValid(candidate)) {
            return;
        }
        resultName = candidate;
        stage.close();
    }

    @FXML
    private void onCancel() {
        stage.close();
    }

    private void updateValidation(String value) {
        String candidate = normalize(value);
        boolean valid = isValid(candidate);
        confirmButton.setDisable(!valid);
        validationLabel.setText(valid ? "" : "Jmeno musi mit 1-14 znaku.");
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isValid(String value) {
        return !value.isEmpty() && value.length() < 15;
    }
}
