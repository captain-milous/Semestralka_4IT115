package cz.vse.semestralka_4it115.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Handles validation and result passing for the player-name dialog.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
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
    private boolean cancelled = true;

    /**
     * Creates player-name dialog controller for JavaFX FXML loading.
     */
    public PlayerNameController() {
    }

    /**
     * Initializes input validation behavior for player name dialog.
     */
    @FXML
    private void initialize() {
        validationLabel.setText("Jmeno musi mit 1-14 znaku.");
        confirmButton.setDisable(true);
        nameField.textProperty().addListener((obs, oldVal, newVal) -> updateValidation(newVal));
    }

    /**
     * Sets owning stage used for closing dialog actions.
     *
     * @param stage dialog stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets initial player name value shown in the input field.
     *
     * @param initialName prefilled player name
     */
    public void setInitialName(String initialName) {
        if (initialName == null) {
            return;
        }
        String normalizedName = normalize(initialName);
        nameField.setText(normalizedName);
        nameField.positionCaret(normalizedName.length());
        updateValidation(normalizedName);
    }

    /**
     * Returns dialog result when confirmed.
     *
     * @return validated player name or {@code null} when canceled
     */
    public String getResult() {
        return cancelled ? null : resultName;
    }

    /**
     * Confirms player name when current value passes validation.
     */
    @FXML
    private void onConfirm() {
        String candidate = normalize(nameField.getText());
        if (!isValid(candidate)) {
            return;
        }
        resultName = candidate;
        cancelled = false;
        stage.close();
    }

    /**
     * Cancels the dialog without saving a new player name.
     */
    @FXML
    private void onCancel() {
        stage.close();
    }

    /**
     * Updates validation label and confirm-button enabled state.
     *
     * @param value current input value
     */
    private void updateValidation(String value) {
        String candidate = normalize(value);
        boolean valid = isValid(candidate);
        confirmButton.setDisable(!valid);
        validationLabel.setText(valid ? "" : "Jmeno musi mit 1-14 znaku.");
    }

    /**
     * Normalizes entered name value before validation.
     *
     * @param value raw input
     * @return trimmed value or empty string
     */
    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    /**
     * Validates player name constraints.
     *
     * @param value normalized name value
     * @return {@code true} when value has 1 to 14 characters
     */
    private boolean isValid(String value) {
        return !value.isEmpty() && value.length() < 15;
    }
}
