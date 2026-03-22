package cz.vse.semestralka_4it115.main;

import cz.vse.semestralka_4it115.logic.space.Difficulty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Displays difficulty-selection dialog from FXML and returns chosen value.
 */
public final class DifficultyDialog {
    private DifficultyDialog() {
    }

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
            return controller.getResultOrDefault();
        } catch (Exception e) {
            return Difficulty.EASY;
        }
    }
}
