package cz.vse.semestralka_4it115.logic.item;

/**
 * The Item class represents a generic object in the game.
 * Each item has a name, a value in crowns, and a weight in kilograms.
 * This class serves as the base class for all specific item types such as Armor, Food, and Weapon.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class Item {
    protected String name;
    protected int value;
    protected double weight;

    //region Constructors

    /**
     * Constructs a new Item with default values.
     * The default name is "No name", the value is 0 crowns, and the weight is 0 kilograms.
     */
    public Item() {
        this.name = "No name";
        this.value = 0;
        this.weight = 0;
    }

    /**
     * Constructs a new Item with the specified name, value, and weight.
     *
     * @param name   the name of the item.
     * @param value  the monetary value of the item in crowns.
     * @param weight the weight of the item in kilograms.
     */
    public Item(String name, int value, double weight) {
        this.name = name;
        this.value = value;
        this.weight = weight;
    }
    //endregion
    //region GET/SET

    /**
     * Returns the name of the item.
     *
     * @return the item's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item.
     *
     * @param name the new name of the item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the monetary value of the item.
     *
     * @return the item's value in crowns.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the monetary value of the item.
     *
     * @param value the new value of the item in crowns.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Returns the weight of the item.
     *
     * @return the item's weight in kilograms.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the weight of the item.
     *
     * @param weight the new weight of the item in kilograms.
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
    //endregion
    //region toString()

    /**
     * Returns a string representation of the item.
     * This includes the item's name, its value in crowns, and its weight in kilograms.
     *
     * @return a formatted string representing the item.
     */
    @Override
    public String toString() {
        return name + " (hodnota: " + value + " korun, váha: " + weight + "kg)";
    }
    //endregion
}
