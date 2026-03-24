package cz.vse.semestralka_4it115.logic.item;

/**
 * The Food class represents an edible item in the game that restores health.
 * In addition to the properties inherited from Item (name, value, weight),
 * Food has a healing value indicating how much health it restores.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class Food extends Item {
    private double heal;

    //region Constructors

    /**
     * Constructs a new Food item with default values.
     * The default healing value is 0.
     */
    public Food() {
        super();
        this.heal = 0;
    }

    /**
     * Constructs a new Food item with the specified name, value, weight, and healing amount.
     *
     * @param name   the name of the food.
     * @param value  the monetary value of the food in crowns.
     * @param weight the weight of the food in kilograms.
     * @param heal   the amount of health restored by the food.
     */
    public Food(String name, int value, double weight, double heal) {
        super(name, value, weight);
        this.heal = heal;
    }
    //endregion
    //region GET/SET

    /**
     * Gets the healing value of the food.
     *
     * @return the healing value.
     */
    public double getHeal() {
        return heal;
    }

    /**
     * Sets the healing value of the food.
     *
     * @param heal the new healing value.
     */
    public void setHeal(double heal) {
        this.heal = heal;
    }
    //endregion
    //region toString()

    /**
     * Returns a string representation of the food.
     *
     * @return a formatted string with details about the food.
     */
    @Override
    public String toString() {
        return name + " (hodnota: " + value + " korun, váha: " + weight + "kg, uzdravení: " + heal + ")";
    }
    //endregion
}
