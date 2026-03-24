package cz.vse.semestralka_4it115.logic.item;

import java.util.ArrayList;
import java.util.List;

/**
 * The Inventory class represents a container for items with a weight limit.
 * It manages a collection of items and ensures that the total weight does not exceed the specified maximum.
 * Methods are provided to add and remove items.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class Inventory {
    private List<Item> items;
    private double maxWeight;
    private double currentWeight;

    //region Constructors

    /**
     * Constructs an Inventory with default settings.
     * Initializes an empty inventory with a maximum weight of 20 and a current weight of 0.
     */
    public Inventory() {
        items = new ArrayList<>();
        maxWeight = 20;
        currentWeight = 0;
    }

    /**
     * Constructs an Inventory with a specified maximum weight.
     * If the provided maxWeight is less than or equal to 20, it defaults to 20.
     *
     * @param maxWeight the maximum weight that can be carried.
     */
    public Inventory(double maxWeight) {
        items = new ArrayList<Item>();
        currentWeight = 0;
        if (maxWeight > 20) {
            this.maxWeight = maxWeight;
        } else {
            this.maxWeight = 20;
        }
    }
    //endregion
    //region GET/SET

    /**
     * Returns the list of items in the inventory.
     *
     * @return the list of items.
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Sets the list of items in the inventory.
     *
     * @param items the new list of items.
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * Returns the maximum weight capacity of the inventory.
     *
     * @return the maximum weight.
     */
    public double getMaxWeight() {
        return maxWeight;
    }

    /**
     * Sets the maximum weight capacity of the inventory.
     *
     * @param maxWeight the new maximum weight.
     */
    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    /**
     * Returns the current total weight of the inventory.
     *
     * @return the current weight.
     */
    public double getCurrentWeight() {
        return currentWeight;
    }

    /**
     * Sets the current total weight of the inventory.
     *
     * @param currentWeight the new current weight.
     */
    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }
    //endregion
    //region toString()

    /**
     * Returns a string representation of the inventory.
     * Lists each item with its details.
     *
     * @return a formatted string listing all inventory items.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Inventory:\n");
        int i = 1;
        for (Item item : items) {
            str.append("\t" + i + ". " + item.toString() + "\n");
            i++;
        }
        return str.toString();
    }
    //endregion
    //region Inventory Operations

    /**
     * Adds an item to the inventory if the total weight after adding does not exceed the maximum weight.
     *
     * @param item the item to add.
     * @throws Exception if adding the item exceeds the inventory's weight limit.
     */
    public void addItem(Item item) throws Exception {
        double tempWeight = item.getWeight() + currentWeight;
        if (tempWeight > maxWeight) {
            throw new Exception("Nelze přidat předmět: celková hmotnost překračuje maximální hmotnostní limit batohu.");
        } else {
            currentWeight = tempWeight;
            items.add(item);
        }
    }

    /**
     * Removes an item from the inventory and updates the current weight.
     *
     * @param item the item to remove.
     */
    public void dropItem(Item item) {
        currentWeight -= item.getWeight();
        if (currentWeight < 0) {
            currentWeight = 0;
        }
        items.remove(item);
    }
    //endregion
}
