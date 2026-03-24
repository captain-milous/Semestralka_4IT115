package cz.vse.semestralka_4it115.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.vse.semestralka_4it115.logic.item.Item;
import cz.vse.semestralka_4it115.logic.space.Map;
import cz.vse.semestralka_4it115.logic.space.Room;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The JsonHandler class provides utility methods for saving and loading a game map to and from a JSON file.
 * It uses the Gson library for serialization and deserialization of Map objects, and registers a custom serializer for Room objects.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class JsonHandler {
    /**
     * The Gson instance configured with pretty printing and a custom Room serializer for JSON operations.
     */
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Room.class, new RoomSerializer())
            .registerTypeAdapter(Item.class, new ItemDeserializer())
            .create();

    /**
     * Saves the given Map object to a file in JSON format.
     *
     * @param map the Map object to be saved.
     * @throws IOException if an I/O error occurs during writing to the file.
     */
    public static void saveMap(Map map) throws IOException {
        String FILE_PATH = "maps\\" + map.getDifficulty().toString() + "\\" + map.getSeed() + ".json";
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(map, writer);
        }
    }

    /**
     * Loads a Map object from a JSON file.
     *
     * @param FILE_PATH the path of the file from which to load the Map object.
     * @return the Map object loaded from the JSON file.
     * @throws IOException if an I/O error occurs during reading from the file.
     */
    public static Map loadMap(String FILE_PATH) throws IOException {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            return gson.fromJson(reader, Map.class);
        }
    }
}
