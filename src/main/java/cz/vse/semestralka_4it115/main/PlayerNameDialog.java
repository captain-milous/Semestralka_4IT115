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
        return requestName(owner, "Nova hra", null);
    }

    public static String requestName(Window owner, String title, String initialName) {
        try {
            FXMLLoader loader = new FXMLLoader(PlayerNameDialog.class.getResource("player-name-dialog.fxml"));
            Parent root = loader.load();
            PlayerNameController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle(title == null || title.isBlank() ? "Nova hra" : title);
            if (owner != null) {
                stage.initOwner(owner);
                stage.initModality(Modality.WINDOW_MODAL);
            } else {
                stage.initModality(Modality.APPLICATION_MODAL);
            }
            stage.setScene(new Scene(root));
            controller.setStage(stage);
            controller.setInitialName(initialName);
            stage.showAndWait();
            return controller.getResult();
        } catch (Exception e) {
            return null;
        }
    }
}
