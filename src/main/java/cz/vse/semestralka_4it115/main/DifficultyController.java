package cz.vse.semestralka_4it115.main;

import cz.vse.semestralka_4it115.logic.space.Difficulty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * Maps difficulty dialog selections to domain difficulty values.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class DifficultyController {
    @FXML
    private ComboBox<String> difficultyCombo;

    private Stage stage;
    private Difficulty result;
    private boolean cancelled = true;

    /**
     * Creates difficulty dialog controller for JavaFX FXML loading.
     */
    public DifficultyController() {
    }

    /**
     * Initializes difficulty combo-box content and default selection.
     */
    @FXML
    private void initialize() {
        difficultyCombo.setItems(FXCollections.observableArrayList("Jednoducha", "Normalni", "Tezka"));
        difficultyCombo.getSelectionModel().selectFirst();
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
     * Returns selected difficulty after confirmation.
     *
     * @return selected difficulty or {@code null} when canceled
     */
    public Difficulty getResult() {
        return cancelled ? null : result;
    }

    /**
     * Confirms selected difficulty and closes dialog.
     */
    @FXML
    private void onConfirm() {
        String selected = difficultyCombo.getSelectionModel().getSelectedItem();
        if ("Normalni".equals(selected)) {
            result = Difficulty.MEDIUM;
        } else if ("Tezka".equals(selected)) {
            result = Difficulty.HARD;
        } else {
            result = Difficulty.EASY;
        }
        cancelled = false;
        stage.close();
    }

    /**
     * Cancels difficulty selection dialog.
     */
    @FXML
    private void onCancel() {
        stage.close();
    }
}
