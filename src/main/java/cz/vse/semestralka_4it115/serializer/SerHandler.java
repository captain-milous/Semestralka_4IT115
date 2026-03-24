package cz.vse.semestralka_4it115.serializer;

import cz.vse.semestralka_4it115.ui.cmd.CMD;
import cz.vse.semestralka_4it115.ui.cmd.LookActions;
import cz.vse.semestralka_4it115.ui.game.TalkablePeople;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles serialization of various game-related maps, allowing saving and loading of command, look action,
 * and conversation data to and from files.
 * <p>
 * Utilizes Java's ObjectOutputStream and ObjectInputStream for reading and writing object data.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class SerHandler {

    /**
     * Saves the given commands map to the specified file path using object serialization.
     *
     * @param FILE_PATH  the path to the file where the map will be saved
     * @param commandMap the map of command strings to CMD objects to serialize
     */
    public static void saveCommandsMap(String FILE_PATH, Map<String, CMD> commandMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {
            oos.writeObject(commandMap);
            System.out.println("Map saved to " + FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a commands map from the specified file path. If loading fails, returns an empty map.
     *
     * @param FILE_PATH the path to the file from which to load the map
     * @return the loaded map of commands, or an empty map if an error occurs
     */
    @SuppressWarnings("unchecked")
    public static Map<String, CMD> loadCommandsMap(String FILE_PATH) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE_PATH))) {
            Object obj = ois.readObject();
            return (Map<String, CMD>) obj;
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();  // fallback
        }
    }

    /**
     * Saves the given look actions map to the specified file path using object serialization.
     *
     * @param FILE_PATH      the path to the file where the look actions map will be saved
     * @param lookActionsMap the map of action strings to LookActions objects to serialize
     */
    public static void saveLookActionsMap(String FILE_PATH, Map<String, LookActions> lookActionsMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {
            oos.writeObject(lookActionsMap);
            System.out.println("Map saved to " + FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a look actions map from the specified file path. If loading fails, returns an empty map.
     *
     * @param FILE_PATH the path to the file from which to load the look actions map
     * @return the loaded map of look actions, or an empty map if an error occurs
     */
    @SuppressWarnings("unchecked")
    public static Map<String, LookActions> loadLookActionsMap(String FILE_PATH) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE_PATH))) {
            Object obj = ois.readObject();
            return (Map<String, LookActions>) obj;
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();  // fallback
        }
    }

    /**
     * Saves the given conversations map to the specified file path using object serialization.
     *
     * @param FILE_PATH        the path to the file where the conversations map will be saved
     * @param conversationsMap the map of TalkablePeople to conversation strings to serialize
     */
    public static void saveConversationsMap(String FILE_PATH, Map<TalkablePeople, String> conversationsMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {
            oos.writeObject(conversationsMap);
            System.out.println("Map saved to " + FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a conversations map from the specified file path. If loading fails, returns an empty map.
     *
     * @param FILE_PATH the path to the file from which to load the conversations map
     * @return the loaded map of TalkablePeople to conversation strings, or an empty map if an error occurs
     */
    @SuppressWarnings("unchecked")
    public static Map<TalkablePeople, String> loadConversationsMap(String FILE_PATH) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE_PATH))) {
            Object obj = ois.readObject();
            return (Map<TalkablePeople, String>) obj;
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();  // fallback
        }
    }
}
