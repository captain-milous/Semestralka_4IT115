package cz.vse.semestralka_4it115.logic.space;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * The Room class represents a location within the game world.
 * Each room has a unique identifier, a name, a description, and a lock status.
 * It maintains lists of items present in the room, available exits to other rooms, and other people currently in the room.
 * Methods are provided for managing these elements.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class Room {
    private int id;
    private String name;
    private String description;
    private boolean isLocked;
    private List<Item> items;
    private List<Integer> exits;
    private List<Person> otherPeople;

    //region Constructors

    /**
     * Constructs a new Room with default values.
     * Default id is -1, name is "No Name", description is "No descripition",
     * unlocked state, and empty lists for items, exits, and other people.
     */
    public Room() {
        this.id = -1;
        this.name = "No Name";
        this.description = "No descripition";
        this.isLocked = false;
        this.items = new ArrayList<>();
        this.exits = new ArrayList<>();
        this.otherPeople = new ArrayList<>();
    }

    /**
     * Constructs a new Room with the specified id, name, and description.
     * Initializes empty lists for items, exits, and other people, with the room unlocked.
     *
     * @param id          the unique identifier for the room.
     * @param name        the name of the room.
     * @param description a brief description of the room.
     */
    public Room(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        items = new ArrayList<Item>();
        exits = new ArrayList<Integer>();
        otherPeople = new ArrayList<Person>();
        this.isLocked = false;
    }

    /**
     * Constructs a new Room with full details.
     *
     * @param id          the unique identifier for the room.
     * @param name        the name of the room.
     * @param description a brief description of the room.
     * @param items       the list of items present in the room.
     * @param exits       the list of exit room IDs.
     * @param otherPeople the list of other people present in the room.
     */
    public Room(int id, String name, String description, List<Item> items, List<Integer> exits, List<Person> otherPeople) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.items = items;
        this.exits = exits;
        this.otherPeople = otherPeople;
        this.isLocked = false;
    }
    //endregion
    //region GET/SET

    /**
     * Returns the room's unique identifier.
     *
     * @return the room id.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the room's unique identifier.
     *
     * @param id the new room id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the room.
     *
     * @return the room name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the room's name.
     *
     * @param name the new room name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the room.
     *
     * @return the room description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the room.
     *
     * @param description the new room description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the list of items in the room.
     *
     * @return the list of items.
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Sets the list of items in the room.
     *
     * @param items the new list of items.
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * Returns the list of exit room IDs.
     *
     * @return the list of exits.
     */
    public List<Integer> getExits() {
        return exits;
    }

    /**
     * Sets the list of exit room IDs.
     *
     * @param exits the new list of exits.
     */
    public void setExits(List<Integer> exits) {
        this.exits = exits;
    }

    /**
     * Returns the list of other people present in the room.
     *
     * @return the list of people.
     */
    public List<Person> getOtherPeople() {
        return otherPeople;
    }

    /**
     * Sets the list of other people in the room.
     *
     * @param otherPeople the new list of people.
     */
    public void setOtherPeople(List<Person> otherPeople) {
        this.otherPeople = otherPeople;
    }

    /**
     * Checks if the room is locked.
     *
     * @return true if the room is locked, false otherwise.
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Sets the lock status of the room.
     *
     * @param isLocked the new lock status.
     */
    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
    //endregion
    //region ToString()

    /**
     * Returns a string representation of the room.
     * If the room is locked, it appends "(Zamčená)" to the room name.
     *
     * @return a formatted string representing the room.
     */
    @Override
    public String toString() {
        if (isLocked()) {
            return this.name + "(Zamčená)";
        }
        return this.name + "\n" + peopleToString() + "\n" + itemsToString();
    }

    /**
     * Returns a formatted string listing all items in the room.
     *
     * @return a string representation of the room's items.
     */
    public String itemsToString() {
        StringBuilder sb = new StringBuilder();
        if (this.items.size() > 1) {
            sb.append("Věci v místnosti:");
            int i = 1;
            for (Item item : items) {
                sb.append("\n  " + i + ". " + item.toString());
                i++;
            }
        } else if (this.items.size() == 1) {
            sb.append("V místnosti se nachází pouze " + items.get(0).toString());
        } else {
            sb.append("V místnosti se nenachází žádné věci");
        }
        return sb.toString();
    }

    /**
     * Returns a formatted string listing all other people in the room.
     *
     * @return a string representation of the people in the room.
     */
    public String peopleToString() {
        StringBuilder sb = new StringBuilder();
        if (this.otherPeople.size() > 1) {
            sb.append("Lidé v místnosti:");
            int i = 1;
            for (Person person : otherPeople) {
                sb.append("\n  " + i + ". " + person.toString());
                i++;
            }
        } else if (this.otherPeople.size() == 1) {
            sb.append("V místnosti se nachází pouze " + otherPeople.get(0).toString());
        } else {
            sb.append("V místnosti se nenachází žádní lidé");
        }
        return sb.toString();
    }
    //endregion
    //region Operations

    /**
     * Adds an item to the room.
     *
     * @param item the item to add.
     * @throws Exception if an error occurs while adding the item.
     */
    public void addItem(Item item) throws Exception {
        items.add(item);
    }

    /**
     * Removes an item from the room.
     *
     * @param item the item to remove.
     * @throws Exception if an error occurs while removing the item.
     */
    public void removeItem(Item item) throws Exception {
        items.remove(item);
    }

    /**
     * Adds an exit (represented by a room ID) to the room.
     * The exit is only added if it does not already exist, is greater than 0, and is not the current room's id.
     *
     * @param exitID the room ID of the exit.
     */
    public void addExit(int exitID) {
        if (!exits.contains(exitID) && exitID > 0 && exitID != this.id) {
            exits.add(exitID);
        }
    }

    /**
     * Removes an exit (represented by a room ID) from the room.
     *
     * @param exitID the room ID of the exit to remove.
     */
    public void removeExit(int exitID) {
        exits.remove(exitID);
    }

    /**
     * Adds a person to the list of people present in the room.
     *
     * @param otherPerson the person to add.
     * @throws Exception if an error occurs while adding the person.
     */
    public void addOtherPeople(Person otherPerson) throws Exception {
        otherPeople.add(otherPerson);
    }

    /**
     * Removes a person from the room.
     *
     * @param otherPerson the person to remove.
     * @throws Exception if an error occurs while removing the person.
     */
    public void removeOtherPeople(Person otherPerson) throws Exception {
        otherPeople.remove(otherPerson);
    }

    /**
     * Marks matching person in this room as dead and peaceful.
     *
     * @param person person instance used for name matching
     */
    public void setPersonDead(Person person) {
        for (Person p : otherPeople) {
            if (p.getName().equals(person.getName())) {
                p.setHealth(0);
                p.setIsAlive(false);
                p.setIsPeaceful(true);
            }
        }
    }
    //endregion
}
