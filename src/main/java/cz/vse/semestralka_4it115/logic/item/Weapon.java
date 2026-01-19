package cz.vse.semestralka_4it115.logic.item;

/**
 * The Weapon class represents a weapon in the game that provides damage capabilities.
 * In addition to the properties inherited from Item (name, value, weight),
 * Weapon has a damage rating indicating its offensive power.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-03-28
 */
public class Weapon extends Item {
    private double damage;

    //region Constructors

    /**
     * Constructs a new Weapon item with default values.
     * The default damage value is 0.
     */
    public Weapon() {
        super();
        this.damage = 0;
    }

    /**
     * Constructs a new Weapon item with the specified name, value, weight, and damage rating.
     *
     * @param name   the name of the weapon.
     * @param value  the monetary value of the weapon in crowns.
     * @param weight the weight of the weapon in kilograms.
     * @param damage the damage bonus provided by the weapon.
     */
    public Weapon(String name, int value, double weight, double damage) {
        super(name, value, weight);
        this.damage = damage;
    }
    //endregion
    //region GET/SET

    /**
     * Gets the damage bonus of the weapon.
     *
     * @return the damage value.
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Sets the damage bonus of the weapon.
     *
     * @param damage the new damage value.
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }
    //endregion
    //region toString()

    /**
     * Returns a string representation of the weapon.
     *
     * @return a formatted string with details about the weapon.
     */
    @Override
    public String toString() {
        return name + " (hodnota: " + value + " korun, váha: " + weight + "kg, poškození: " + damage + ")";
    }
    //endregion
}
