package cz.vse.semestralka_4it115.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Displays player-name dialog from FXML and returns validated result.
 */
public final class PlayerNameDialog {
    private PlayerNameDialog() {
    }

    public static String requestName(Window owner) {
        try {
            FXMLLoader loader = new FXMLLoader(PlayerNameDialog.class.getResource("player-name-dialog.fxml"));
            Parent root = loader.load();
            PlayerNameController controller = loader.getController();

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
            return "Hrac";
        }
    }
}
