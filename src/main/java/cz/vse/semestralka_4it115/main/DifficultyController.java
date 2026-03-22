package cz.vse.semestralka_4it115.main;

import cz.vse.semestralka_4it115.logic.space.Difficulty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * Controller for difficulty dialog.
 */
public class DifficultyController {
    @FXML
    private ComboBox<String> difficultyCombo;

    private Stage stage;
    private Difficulty result;
    private boolean cancelled = true;

    @FXML
    private void initialize() {
        difficultyCombo.setItems(FXCollections.observableArrayList("Jednoducha", "Normalni", "Tezka"));
        difficultyCombo.getSelectionModel().selectFirst();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Difficulty getResult() {
        return cancelled ? null : result;
    }

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

    @FXML
    private void onCancel() {
        stage.close();
    }
}
