package cz.vse.semestralka_4it115.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Handles the end-game dialog content and button actions.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class EndGameController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label subtitleLabel;

    private Stage stage;
    private Runnable onRestart;
    private Runnable onExit;

    /**
     * Creates end-game dialog controller for JavaFX FXML loading.
     */
    public EndGameController() {
    }

    /**
     * Initializes dialog labels and callbacks according to game result.
     *
     * @param stage dialog stage
     * @param win whether the game ended with victory
     * @param onRestart callback for restart button
     * @param onExit callback for exit button
     */
    public void init(Stage stage, boolean win, Runnable onRestart, Runnable onExit) {
        this.stage = stage;
        this.onRestart = onRestart;
        this.onExit = onExit;
        if (win) {
            titleLabel.setText("Gratulace!");
            subtitleLabel.setText("Vyhral jsi. Chces hrat znovu?");
        } else {
            titleLabel.setText("Prohra");
            subtitleLabel.setText("Bohuzel jsi zemrel. Chces to zkusit znovu?");
        }
    }

    /**
     * Closes dialog and triggers restart callback when available.
     */
    @FXML
    private void onRestartClicked() {
        stage.close();
        if (onRestart != null) {
            onRestart.run();
        }
    }

    /**
     * Closes dialog and triggers exit callback when available.
     */
    @FXML
    private void onExitClicked() {
        stage.close();
        if (onExit != null) {
            onExit.run();
        }
    }
}
