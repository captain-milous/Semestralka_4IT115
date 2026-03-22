package cz.vse.semestralka_4it115.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for end-game dialog.
 */
public class EndGameController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label subtitleLabel;

    private Stage stage;
    private Runnable onRestart;
    private Runnable onExit;

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

    @FXML
    private void onRestartClicked() {
        stage.close();
        if (onRestart != null) {
            onRestart.run();
        }
    }

    @FXML
    private void onExitClicked() {
        stage.close();
        if (onExit != null) {
            onExit.run();
        }
    }
}
