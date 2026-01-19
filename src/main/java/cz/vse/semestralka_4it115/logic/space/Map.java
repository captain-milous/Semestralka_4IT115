package cz.vse.semestralka_4it115.logic.space;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.item.Item;

import java.util.List;
import java.util.ArrayList;

/**
 * The Map class represents the overall game map.
 * It holds the current room, a layout (list of rooms), a game difficulty level, and an optional seed for map generation.
 * Methods are provided to manage rooms and retrieve information about the current room and its exits.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-03-28
 */
public class Map {
    private String seed;
    private Difficulty difficulty;
    private Room currentRoom;
    private List<Room> layout;

    //region Constructors

    /**
     * Constructs a new Map with default values.
     * The default difficulty is EASY, and the layout is initialized as an empty list.
     */
    public Map() {
        this.seed = null;
        this.difficulty = Difficulty.EASY;
        this.currentRoom = null;
        this.layout = new ArrayList<>();
    }

    /**
     * Constructs a new Map with the specified seed, difficulty, current room, and layout.
     *
     * @param seed        a seed value used for map generation.
     * @param difficulty  the game difficulty.
     * @param currentRoom the room where the player is currently located.
     * @param layout      the complete list of rooms in the map.
     */
    public Map(String seed, Difficulty difficulty, Room currentRoom, List<Room> layout) {
        this.seed = seed;
        this.difficulty = difficulty;
        this.currentRoom = currentRoom;
        this.layout = layout;
    }
    //endregion
    //region GET/SET

    /**
     * Returns the seed value as a string.
     *
     * @return the seed.
     */
    public String getSeed() {
        return seed.toString();
    }

    /**
     * Sets the seed value.
     *
     * @param seed the new seed value.
     */
    public void setSeed(String seed) {
        this.seed = seed;
    }

    /**
     * Returns the difficulty level.
     *
     * @return the difficulty.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level.
     *
     * @param difficulty the new difficulty.
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Returns the current room.
     *
     * @return the current room.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Sets the current room.
     *
     * @param currentRoom the new current room.
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Returns the complete layout (list of rooms) of the map.
     *
     * @return the layout.
     */
    public List<Room> getLayout() {
        return layout;
    }

    /**
     * Sets the layout (list of rooms) of the map.
     *
     * @param layout the new layout.
     */
    public void setLayout(List<Room> layout) {
        this.layout = layout;
    }
    //endregion
    //region ToString()

    /**
     * Returns a formatted string representation of the map.
     * Lists all rooms in the layout with their details.
     *
     * @return a string representing the map layout.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        String sep = "————————————————————————————————————————\n";
        int i = 1;
        for (Room room : layout) {
            str.append(sep);
            str.append(i + ". " + room.toString() + "\n");
            i++;
        }
        str.append(sep + "\n");
        return str.toString();
    }

    /**
     * Returns a formatted string describing the exits from the current room.
     *
     * @return a string representation of the current room's exits.
     */
    public String currentExitsToString() {
        StringBuilder str = new StringBuilder();
        List<Room> exitList = getCurrentExits();
        //str.append("Nacházíš se v místnosti " + this.currentRoom.getName() + "\n");
        if (exitList.size() > 1) {
            str.append("Východy z této místnosti vedou do:");
            int i = 1;
            for (Room room : exitList) {
                if (exitList.get(0).isLocked()) {
                    str.append("\n  " + i + ". " + room.getName() + " (Zamčená)");
                } else {
                    str.append("\n  " + i + ". " + room.getName());
                }
                i++;
            }
        } else if (exitList.size() == 1) {
            if (exitList.get(0).isLocked()) {
                str.append("Východ z této místností vede do " + exitList.get(0).getName() + " (Zamčená)");
            } else {
                str.append("Východ z této místností vede do " + exitList.get(0).getName());
            }
        } else {
            str.append("Tato místnost nemá žádné východy.");
        }
        return str.toString();
    }

    /**
     * Executes curRoomToString functionality.
     *
     * @return the String result.
     */
    public String curRoomToString() {
        String output = getCurrentRoom().toString();
        output += "\n" + currentExitsToString();
        return output;
    }
    //endregion
    //region Operations

    /**
     * Adds a new room to the map layout.
     * If a room with the same id already exists, an IllegalArgumentException is thrown.
     *
     * @param room the room to add.
     */
    public void addRoom(Room room) {
        for (Room r : layout) {
            if (r.getId() == room.getId()) {
                throw new IllegalArgumentException("Room already exists");
            }
        }
        layout.add(room);
    }

    /**
     * Retrieves a room by its id.
     *
     * @param id the id of the room.
     * @return the matching room, or a default Room if not found.
     */
    public Room getRoom(int id) {
        Room room = new Room();
        for (Room r : layout) {
            if (r.getId() == id) {
                room = r;
            }
        }
        return room;
    }

    /**
     * Removes a room from the map layout by its id.
     *
     * @param id the id of the room to remove.
     * @throws IllegalArgumentException if the room does not exist.
     */
    public void removeRoom(int id) {
        Room room = new Room();
        for (Room r : layout) {
            if (r.getId() == id) {
                room = r;
            }
        }
        if (room.getId() == -1) {
            throw new IllegalArgumentException("Room does not exist");
        } else {
            layout.remove(room);
        }
    }

    /**
     * Returns a list of rooms that are accessible from the current room.
     *
     * @return the list of current exits.
     */
    public List<Room> getCurrentExits() {
        List<Room> exits = new ArrayList<>();
        List<Integer> exitIds = this.currentRoom.getExits();
        for (Room room : layout) {
            if (exitIds.contains(room.getId())) {
                exits.add(room);
            }
        }
        return exits;
    }

    /**
     * Returns the id of the current room.
     *
     * @return the current room id.
     */
    public int getCurrentId() {
        return this.currentRoom.getId();
    }

    /**
     * Returns the name of the current room.
     *
     * @return the current room name.
     */
    public String getCurrentName() {
        return this.currentRoom.getName();
    }

    /**
     * Returns the description of the current room.
     *
     * @return the current room description.
     */
    public String getCurrentDescription() {
        return this.currentRoom.getDescription();
    }

    /**
     * Returns a list of other people present in the current room.
     *
     * @return the list of people in the current room.
     */
    public List<Person> getCurrentOtherPeople() {
        return this.currentRoom.getOtherPeople();
    }

    /**
     * Returns a list of items present in the current room.
     *
     * @return the list of items in the current room.
     */
    public List<Item> getCurrentItems() {
        return this.currentRoom.getItems();
    }
    //endregion
}