package cz.vse.semestralka_4it115.serializer;

import java.io.*;

/**
 * The TxtHandler class handles loading of text files into strings.
 * <p>
 * Provides a method to read an entire UTF-8 encoded text file and return its contents.
 * If loading fails, returns an error message.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-04-03
 */
public class TxtHandler {

    /**
     * Loads the entire text content of the specified UTF-8 file into a string.
     *
     * @param FILE_PATH the path to the text file to load
     * @return the contents of the file as a string, or an error message if loading fails
     */
    public String loadTXT(String FILE_PATH) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH, java.nio.charset.StandardCharsets.UTF_8))) {
            String line;
            String text = "";
            while ((line = br.readLine()) != null) {
                text += line + "\n";
            }
            return text;
        } catch (IOException | NullPointerException e) {
            return "Nepodařilo se načíst nápovědu: " + e.getMessage();
        }
    }
}
