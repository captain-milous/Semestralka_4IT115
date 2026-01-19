package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.serializer.JsonHandler;
import cz.vse.semestralka_4it115.logic.space.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Provides methods to manage game loading and map selection based on difficulty.
 * <p>
 * Handles reading JSON map files from a directory structure organized by difficulty
 * and initializes the Game instance accordingly.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-04-03
 */
public class GameHandler {
    public static Game game = new Game();

    /**
     * Constructs a new GameHandler and initializes a default Game instance.
     */
    public GameHandler() {

    }

    /**
     * Sets the difficulty level for the current game.
     *
     * @param difficulty the Difficulty enum representing the desired difficulty level
     */
    public static void setGameDifficulty(Difficulty difficulty) {
        game.setDifficulty(difficulty);
    }

    /**
     * Retrieves the current game map.
     *
     * @return the Map object for the current game
     */
    public static Map getMap() {
        return game.getMap();
    }

    /**
     * Imports a map for the current game based on the selected difficulty.
     * Chooses a random JSON file from the difficulty-specific directory and loads it.
     *
     * @throws FileNotFoundException if no suitable JSON file can be found or read
     */
    public static void ImportMap() throws FileNotFoundException {
        String filePath = "maps\\" + game.getDifficulty().toString() + "\\";
        try {
            String fileName = getRandomMapFile(filePath);
            filePath = filePath + fileName;
            game.setMap(JsonHandler.loadMap(filePath));
        } catch (Exception e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    /**
     * Returns a list of all JSON file names in the specified directory.
     *
     * @param filePath the directory path to search for JSON files
     * @return a list of JSON file names in the directory
     */
    private static List<String> getAllJsonFiles(String filePath) {
        List<String> jsonFiles = new ArrayList<>();
        File directory = new File(filePath);

        if (directory.exists() && directory.isDirectory()) {
            String[] files = directory.list();
            if (files != null) {
                for (String fileName : files) {
                    if (fileName.toLowerCase().endsWith(".json")) {
                        jsonFiles.add(fileName);
                    }
                }
            }
        }

        return jsonFiles;
    }

    /**
     * Selects a random file name from the list of JSON files in the given directory.
     *
     * @param filePath the directory path to pick a random file from
     * @return a randomly selected JSON file name from the directory
     * @throws IllegalStateException if the directory contains no JSON files
     */
    private static String getRandomMapFile(String filePath) throws IllegalStateException {
        List<String> folderNames = getAllJsonFiles(filePath);
        if (folderNames.isEmpty()) {
            throw new IllegalStateException("V daném adresáři se nenacházi žádná mapa v požadovaném formátu.");
        }
        int randomIndex = new Random().nextInt(folderNames.size());

        return folderNames.get(randomIndex);
    }
}
