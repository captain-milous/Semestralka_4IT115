// File: logic.item.FoodTest.java
package cz.vse.semestralka_4it115.logic.item;

import cz.vse.semestralka_4it115.logic.item.Food;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FoodTest {

    @Test
    void testDefaultConstructor() {
        Food food = new Food();
        // Default Item fields
        assertEquals("No name", food.getName());
        assertEquals(0, food.getValue());
        assertEquals(0.0, food.getWeight());
        // Default heal
        assertEquals(0.0, food.getHeal());
        String expected = "No name (hodnota: 0 korun, váha: 0.0kg, uzdravení: 0.0)";
        assertEquals(expected, food.toString());
    }

    @Test
    void testParameterizedConstructorAndGetHeal() {
        Food food = new Food("Bread", 10, 1.0, 5.0);
        assertEquals("Bread", food.getName());
        assertEquals(10, food.getValue());
        assertEquals(1.0, food.getWeight());
        assertEquals(5.0, food.getHeal());

        food.setHeal(7.5);
        assertEquals(7.5, food.getHeal());

        String expected = "Bread (hodnota: 10 korun, váha: 1.0kg, uzdravení: 7.5)";
        assertEquals(expected, food.toString());
    }
}
