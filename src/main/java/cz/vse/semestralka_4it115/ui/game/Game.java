package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.exception.EnemyException;
import cz.vse.semestralka_4it115.exception.LockException;
import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.item.Item;
import cz.vse.semestralka_4it115.logic.space.*;

import java.text.Normalizer;
import java.util.List;

/**
 * The Game class handles game functionality, including player movement, inventory access,
 * and room transitions. It maintains the game state, such as current map, player, and difficulty.
 * Methods allow moving between rooms, searching inventory, and resetting player state.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class Game {
    private Map map;
    private Person player;
    private Difficulty difficulty;

    /**
     * Constructs a new Game with default settings, initializing a new map and player.
     */
    public Game() {
        map = new Map();
        player = new Person();
        difficulty = Difficulty.EASY;
    }

    /**
     * Constructs a new Game with the specified difficulty, initializing a new map and player.
     *
     * @param difficulty the Difficulty level to use for this game
     */
    public Game(Difficulty difficulty) {
        map = new Map();
        player = new Person();
        this.difficulty = difficulty;
    }

    /**
     * Moves the player to another room based on the given input.
     * Parses the input to select an exit from the current room, verifies lock and enemy conditions,
     * and updates the current room accordingly.
     *
     * @param roomInput the input string indicating which exit to take
     * @throws Exception if the input is invalid, no valid exit exists, or the room is locked without a key
     */
    public void goToNextRoom(String roomInput) throws Exception {
        int roomIndex = 0;
        Boolean isNumber = true;
        Boolean enemy = isInCurrRoomEnemy();
        try {
            roomIndex = Integer.parseInt(roomInput);
        } catch (NumberFormatException ignored) {
            isNumber = false;
            roomIndex = 0;
        }
        if (enemy) {
            throw new EnemyException("V místnosti se nachází nepřítel, kterého musíš nejdříve porazit než půjdeš dál.");
        }
        if (isNumber) {
            roomIndex = roomIndex - 1;
            List<Room> exits = map.getCurrentExits();
            if (exits.isEmpty() || roomIndex >= exits.size() || roomIndex < 0) {
                throw new Exception("Místnost nemá žádné východy, nebo index, který poslal uživatel je mimo kapacitu exitů.");
            } else {
                Room room = exits.get(roomIndex);
                if (room.isLocked()) {
                    if (searchInInventory("Klic") != null) {
                        map.setCurrentRoom(room);
                    } else {
                        throw new Exception("Požadovaná místnost je zamčená. Najdi klíč.");
                    }
                } else {
                    map.setCurrentRoom(room);
                }
            }
        } else {
            List<Integer> tempExits = getCurrentRoom().getExits();
            List<Room> tempRooms = getMap().getLayout();
            String inputRoomName = roomInput.toLowerCase();
            Boolean exists = false;
            for (Room r : tempRooms) {
                if (r.getName().toLowerCase().startsWith(inputRoomName)) {
                    roomIndex = r.getId();
                    for (Integer i : tempExits) {
                        if (i == roomIndex) {
                            if (r.isLocked()) {
                                if (searchInInventory("Klic") != null) {
                                    map.setCurrentRoom(r);
                                } else {
                                    throw new LockException("Požadovaná místnost je zamčená. Najdi klíč.");
                                }
                            } else {
                                map.setCurrentRoom(r);
                            }
                            exists = true;
                            break;
                        }
                    }
                }
                if (exists) {
                    break;
                }
            }
            if (!exists) {
                throw new Exception("Místnost s tím názvem neexistuje.");
            }
        }
    }

    /**
     * Checks if there is any enemy present in the current room.
     *
     * @return true if an enemy is present, false otherwise
     */
    private Boolean isInCurrRoomEnemy() {
        List<Person> otherPeople = map.getCurrentOtherPeople();
        for (Person p : otherPeople) {
            if (!p.isPeaceful()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches the player's inventory for an item with a name starting with the given string.
     *
     * @param itemName the name or prefix of the item to search for
     * @return the matching Item if found, otherwise null
     */
    public Item searchInInventory(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            return null;
        }
        List<Item> items = getPlayer().getInventory().getItems();
        String normalizedInput = normalizeText(itemName);
        for (Item item : items) {
            String normalizedItemName = normalizeText(item.getName());
            if (normalizedItemName.startsWith(normalizedInput)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Normalizes text for accent-insensitive matching.
     */
    private String normalizeText(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

    /**
     * Retrieves the current room the player is in.
     *
     * @return the current Room object
     */
    public Room getCurrentRoom() {
        return map.getCurrentRoom();
    }

    /**
     * Sets the player for the game.
     *
     * @param player the Person object representing the new player
     */
    public void setPlayer(Person player) {
        this.player = player;
    }

    /**
     * Retrieves the current player.
     *
     * @return the Person object representing the player
     */
    public Person getPlayer() {
        return player;
    }

    /**
     * Retrieves the difficulty level of the game.
     *
     * @return the current Difficulty setting
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level for the game.
     *
     * @param difficulty the Difficulty enum representing the desired difficulty level
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Retrieves the game map.
     *
     * @return the Map object representing the game world
     */
    public Map getMap() {
        return map;
    }

    /**
     * Sets the game map.
     *
     * @param map the Map object to use as the game world
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * Sets the current room for the game.
     *
     * @param room the Room object to set as current
     */
    public void setCurrentRoom(Room room) {
        map.setCurrentRoom(room);
    }

    /**
     * Resets the player with a new Person object and sets the provided name.
     *
     * @param name the name to assign to the new player
     */
    public void resetPlayer(String name) {
        player = new Person();
        player.setName(name);
    }

    /**
     * Returns a string representation of the current room's exits.
     *
     * @return a string listing available exits from the current room
     */
    public String currentExitsToString() {
        return map.currentExitsToString();
    }
}
