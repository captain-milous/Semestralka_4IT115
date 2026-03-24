package cz.vse.semestralka_4it115.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Opens end-game popup and wires restart/exit callbacks.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public final class EndGameDialog {
    private EndGameDialog() {
    }

    /**
     * Shows end-game dialog as modal window.
     *
     * @param owner owner window for modal dialog
     * @param win whether the game ended with victory
     * @param onRestart callback executed when restart is selected
     * @param onExit callback executed when exit is selected
     */
    public static void show(Window owner, boolean win, Runnable onRestart, Runnable onExit) {
        try {
            FXMLLoader loader = new FXMLLoader(EndGameDialog.class.getResource("end-game-dialog.fxml"));
            Parent root = loader.load();
            EndGameController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Konec hry");
            if (owner != null) {
                stage.initOwner(owner);
                stage.initModality(Modality.WINDOW_MODAL);
            } else {
                stage.initModality(Modality.APPLICATION_MODAL);
            }

            controller.init(stage, win, onRestart, onExit);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            if (onExit != null) {
                onExit.run();
            }
        }
    }
}
