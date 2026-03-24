package cz.vse.semestralka_4it115.main;

import cz.vse.semestralka_4it115.ui.game.GameUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launches the application in JavaFX or text mode based on startup arguments.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class Main extends Application {
    /**
     * Creates application bootstrap instance.
     */
    public Main() {
    }

    /**
     * Starts the application in text mode when argument {@code text} is provided, otherwise opens GUI.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        if(args.length > 0 && args[0].equals("text")){
            GameUI gui = new GameUI();
            gui.start();
            Platform.exit();
        }
        else {
            launch();
        }

    }

    /**
     * Initializes and shows the primary JavaFX stage with the main game view.
     *
     * @param primaryStage primary JavaFX window
     * @throws Exception when the FXML view cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Adventura - Kraluv posel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
