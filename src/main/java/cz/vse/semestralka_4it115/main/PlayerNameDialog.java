package cz.vse.semestralka_4it115.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Opens the player-name dialog and returns a validated player name.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public final class PlayerNameDialog {
    private PlayerNameDialog() {
    }

    /**
     * Shows player-name dialog with default title.
     *
     * @param owner owner window for modal dialog
     * @return validated player name or {@code null} when canceled
     */
    public static String requestName(Window owner) {
        return requestName(owner, "Nova hra", null);
    }

    /**
     * Shows player-name dialog with configurable title and prefilled value.
     *
     * @param owner owner window for modal dialog
     * @param title window title
     * @param initialName initial value in input field
     * @return validated player name or {@code null} when canceled/error occurs
     */
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
