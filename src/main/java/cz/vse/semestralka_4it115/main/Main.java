package cz.vse.semestralka_4it115.main;

import cz.vse.semestralka_4it115.ui.game.GameUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Main class handles main functionality.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-04-03
 */
public class Main extends Application {

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