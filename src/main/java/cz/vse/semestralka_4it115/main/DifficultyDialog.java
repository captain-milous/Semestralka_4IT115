package cz.vse.semestralka_4it115.main;

import cz.vse.semestralka_4it115.logic.space.Difficulty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Opens the difficulty selection dialog and returns selected game difficulty.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public final class DifficultyDialog {
    private DifficultyDialog() {
    }

    /**
     * Shows modal difficulty dialog.
     *
     * @param owner owner window for modal dialog
     * @return selected difficulty or {@code null} when canceled/error occurs
     */
    public static Difficulty requestDifficulty(Window owner) {
        try {
            FXMLLoader loader = new FXMLLoader(DifficultyDialog.class.getResource("difficulty-dialog.fxml"));
            Parent root = loader.load();
            DifficultyController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Nova hra");
            if (owner != null) {
                stage.initOwner(owner);
                stage.initModality(Modality.WINDOW_MODAL);
            } else {
                stage.initModality(Modality.APPLICATION_MODAL);
            }
            stage.setScene(new Scene(root));
            controller.setStage(stage);
            stage.showAndWait();
            return controller.getResult();
        } catch (Exception e) {
            return null;
        }
    }
}
