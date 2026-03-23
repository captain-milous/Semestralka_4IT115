package cz.vse.semestralka_4it115.main;

import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Opens local HTML help in a dedicated window.
 */
public final class HelpHtmlDialog {
    private static final Path HELP_HTML_PATH = Path.of("resources", "help.html");

    private HelpHtmlDialog() {
    }

    public static void show(Window owner) {
        Path absoluteHelpPath = HELP_HTML_PATH.toAbsolutePath();
        if (!Files.exists(absoluteHelpPath)) {
            throw new IllegalStateException("Soubor nenalezen: " + absoluteHelpPath);
        }

        WebView webView = new WebView();
        webView.getEngine().load(absoluteHelpPath.toUri().toString());

        Stage stage = new Stage();
        stage.setTitle("Napoveda");
        if (owner != null) {
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
        } else {
            stage.initModality(Modality.APPLICATION_MODAL);
        }
        stage.setScene(new Scene(webView, 900, 640));
        stage.showAndWait();
    }
}
