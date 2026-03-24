package cz.vse.semestralka_4it115.logic.entity;

import cz.vse.semestralka_4it115.logic.item.Armor;
import cz.vse.semestralka_4it115.logic.item.Inventory;
import cz.vse.semestralka_4it115.logic.item.Item;
import cz.vse.semestralka_4it115.logic.item.Weapon;

/**
 * The Person class represents an entity in the game with attributes such as name, strength, defence, health, and an inventory.
 * It provides methods for health management, combat actions, and item transactions.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class Person {
    protected String name;
    protected double strength;
    protected double defence;
    protected double maxHealth;
    protected double health;
    protected boolean isAlive;
    protected boolean isPeaceful;
    protected int money;
    protected Inventory inventory;

    //region Constructors

    /**
     * Constructs a new Person with default values.
     * The default name is "No name", strength and defence are 0, maximum health is 100,
     * the person starts alive and peaceful with 0 money and an inventory capacity of 20.
     */
    public Person() {
        this.name = "No name";
        this.strength = 20;
        this.defence = 20;
        this.maxHealth = 100;
        this.health = maxHealth;
        this.isAlive = true;
        this.isPeaceful = true;
        this.money = 30;
        this.inventory = new Inventory(20);
    }

    /**
     * Constructs a new Person with the specified name, strength, and defence.
     * Other attributes are set to default values: maximum health is 100, alive, peaceful, 0 money, and an inventory capacity of 20.
     *
     * @param name     the name of the person.
     * @param strength the strength attribute.
     * @param defence  the defence attribute.
     */
    public Person(String name, double strength, double defence) {
        this.name = name;
        this.strength = strength;
        this.defence = defence;
        this.maxHealth = 100;
        this.health = maxHealth;
        this.isAlive = true;
        this.isPeaceful = true;
        this.money = 30;
        this.inventory = new Inventory(20);
    }

    /**
     * Constructs a new Person with all specified attributes.
     *
     * @param name       the name of the person.
     * @param strength   the strength attribute.
     * @param defence    the defence attribute.
     * @param maxHealth  the maximum health value.
     * @param isAlive    indicates whether the person is alive.
     * @param isPeaceful indicates whether the person is peaceful.
     * @param money      the amount of money the person has.
     * @param inventory  the inventory assigned to the person.
     */
    public Person(String name, double strength, double defence, double maxHealth, boolean isAlive, boolean isPeaceful, int money, Inventory inventory) {
        this.name = name;
        this.strength = strength;
        this.defence = defence;
        this.maxHealth = maxHealth;
        this.isAlive = isAlive;
        if (isAlive) {
            this.health = maxHealth;
        } else {
            this.health = 0;
        }
        this.isPeaceful = isPeaceful;
        this.money = money;
        this.inventory = inventory;
    }
    //endregion
    //region GET/SET

    /**
     * Returns the name of the person.
     *
     * @return the person's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the person.
     *
     * @param name the new name for the person.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the strength attribute.
     *
     * @return the person's strength.
     */
    public double getStrength() {
        return strength;
    }

    /**
     * Sets the strength attribute.
     *
     * @param strength the new strength value.
     */
    public void setStrength(double strength) {
        this.strength = strength;
    }

    /**
     * Returns the defence attribute.
     *
     * @return the person's defence.
     */
    public double getDefence() {
        return defence;
    }

    /**
     * Sets the defence attribute.
     *
     * @param defence the new defence value.
     */
    public void setDefence(double defence) {
        this.defence = defence;
    }

    /**
     * Returns the maximum health value.
     *
     * @return the maximum health.
     */
    public double getMaxHealth() {
        return maxHealth;
    }

    /**
     * Sets the maximum health value.
     *
     * @param maxHealth the new maximum health.
     */
    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * Returns the current health value.
     *
     * @return the current health.
     */
    public double getHealth() {
        return health;
    }

    /**
     * Sets the current health value.
     *
     * @param health the new current health.
     */
    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * Checks if the person is alive.
     *
     * @return true if alive, false otherwise.
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Sets the alive status.
     *
     * @param alive the new alive status.
     */
    public void setIsAlive(boolean alive) {
        isAlive = alive;
    }

    /**
     * Checks if the person is peaceful.
     *
     * @return true if peaceful, false otherwise.
     */
    public boolean isPeaceful() {
        return isPeaceful;
    }

    /**
     * Sets the peaceful status.
     *
     * @param peaceful the new peaceful status.
     */
    public void setIsPeaceful(boolean peaceful) {
        isPeaceful = peaceful;
    }

    /**
     * Returns the inventory of the person.
     *
     * @return the inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Sets the inventory of the person.
     *
     * @param inventory the new inventory.
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Returns the amount of money the person has.
     *
     * @return the money.
     */
    public int getMoney() {
        return money;
    }

    /**
     * Sets the amount of money the person has.
     *
     * @param money the new money amount.
     */
    public void setMoney(int money) {
        this.money = money;
    }
    //endregion
    //region toString()

    /**
     * Returns a string representation of the person.
     * If the person is dead, returns "Mrtvola" (meaning corpse).
     *
     * @return the person's name if alive, or "Mrtvola" if not.
     */
    @Override
    public String toString() {
        if (!isAlive()) {
            return "Mrtvola";
        } else {
            if (isPeaceful) {
                return this.name;
            } else {
                return this.name + " (nepřítel)";
            }
        }
    }

    /**
     * Returns a detailed string representation of the person, including health, strength, defence, and money.
     *
     * @return a formatted string with the person's detailed attributes.
     */
    public String fullToString() {
        if (!isAlive()) {
            return "Mrtvola (peníze: " + this.money + " korun)";
        } else {
            return this.name + " (zdraví: " + this.health + "/" + this.maxHealth + ", síla: " + getEffectiveStrength() + ", obrana: " + getEffectiveDefense() + ", peníze: " + money + " korun)";
        }
    }
    //endregion
    //region Combat and Inventory Operations

    /**
     * Increases the person's health by the specified amount, without exceeding the maximum health.
     *
     * @param heal the amount of health to add.
     */
    public void addHealth(double heal) {
        double tempHealth = health + heal;
        if (tempHealth > maxHealth) {
            health = maxHealth;
        } else {
            health = tempHealth;
        }
    }

    /**
     * Reduces the person's health by the given damage amount.
     * If health drops to 0 or below, the person is marked as dead.
     *
     * @param damage the damage amount.
     */
    public void takeDamage(double damage) {
        double tempHealth = health - damage;
        if (tempHealth <= 0) {
            health = 0;
            isAlive = false;
            isPeaceful = true;
        } else {
            health = tempHealth;
        }
    }

    /**
     * Adds an item to the person's inventory.
     *
     * @param item the item to add.
     * @throws Exception if the item cannot be added due to inventory constraints.
     */
    public void addItemToInventory(Item item) throws Exception {
        inventory.addItem(item);
    }

    /**
     * Removes an item from the person's inventory.
     *
     * @param item the item to remove.
     * @throws Exception if the item cannot be removed.
     */
    public void dropItemFromInventory(Item item) throws Exception {
        inventory.dropItem(item);
    }

    /**
     * Purchases an item by deducting its value from the person's money and adding it to the inventory.
     *
     * @param item the item to buy.
     * @throws Exception if the person does not have enough money.
     */
    public void buyItem(Item item) throws Exception {
        int tempMoney = money - item.getValue();
        if (tempMoney >= 0) {
            addItemToInventory(item);
            this.money = tempMoney;
        } else {
            throw new Exception("You don't have enough money to buy this item.");
        }
    }

    /**
     * Sells an item by removing it from the inventory and adding its value to the person's money.
     *
     * @param item the item to sell.
     * @throws Exception if the item cannot be sold.
     */
    public void sellItem(Item item) throws Exception {
        dropItemFromInventory(item);
        this.money = money + item.getValue();
    }

    /**
     * Calculates the effective strength by summing the base strength with bonuses from weapons in the inventory.
     *
     * @return the effective strength.
     */
    public double getEffectiveStrength() {
        double bonus = 0;
        for (Item item : inventory.getItems()) {
            if (item instanceof Weapon) {
                double dam = ((Weapon) item).getDamage();
                bonus += Math.round((dam / 10) * 10.0) / 10;  // ensures proper rounding of weapon damage bonus
            }
        }
        return this.strength + bonus;
    }

    /**
     * Calculates the effective defence by summing the base defence with bonuses from armor in the inventory.
     *
     * @return the effective defence.
     */
    public double getEffectiveDefense() {
        double bonus = 0;
        for (Item item : inventory.getItems()) {
            if (item instanceof Armor) {
                double def = ((Armor) item).getDefense();
                bonus += Math.round((def / 10) * 10.0) / 10;  // ensures proper rounding of armor defence bonus
            }
        }
        return this.defence + bonus;
    }
    //endregion
}
