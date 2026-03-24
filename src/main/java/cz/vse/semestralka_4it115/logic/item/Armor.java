package cz.vse.semestralka_4it115.logic.item;

/**
 * The Armor class represents protective equipment in the game that provides a defense bonus.
 * In addition to the properties inherited from Item (name, value, weight),
 * Armor has a defense rating indicating the level of protection it offers.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class Armor extends Item {
    private double defense;

    //region Constructors

    /**
     * Constructs a new Armor item with default values.
     * The default defense value is 0.
     */
    public Armor() {
        super();
        this.defense = 0;
    }

    /**
     * Constructs a new Armor item with the specified name, value, weight, and defense rating.
     *
     * @param name    the name of the armor.
     * @param value   the monetary value of the armor in crowns.
     * @param weight  the weight of the armor in kilograms.
     * @param defense the defense bonus provided by the armor.
     */
    public Armor(String name, int value, double weight, double defense) {
        super(name, value, weight);
        this.defense = defense;
    }
    //endregion
    //region GET/SET

    /**
     * Gets the defense bonus of the armor.
     *
     * @return the defense value.
     */
    public double getDefense() {
        return defense;
    }

    /**
     * Sets the defense bonus of the armor.
     *
     * @param defense the new defense value.
     */
    public void setDefense(double defense) {
        this.defense = defense;
    }
    //endregion
    //region toString()

    /**
     * Returns a string representation of the armor.
     *
     * @return a formatted string with details about the armor.
     */
    @Override
    public String toString() {
        return name + " (hodnota: " + value + " korun, váha: " + weight + "kg, obrana: " + defense + ")";
    }
    //endregion
}
