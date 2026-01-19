// File: logic.item.ItemTest.java
package cz.vse.semestralka_4it115.logic.item;

import cz.vse.semestralka_4it115.logic.item.Item;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testDefaultConstructor() {
        Item item = new Item();
        assertEquals("No name", item.getName());
        assertEquals(0, item.getValue());
        assertEquals(0.0, item.getWeight());
        assertEquals("No name (hodnota: 0 korun, váha: 0.0kg)", item.toString());
    }

    @Test
    void testParameterizedConstructorAndGettersSetters() {
        Item item = new Item("Sword", 100, 5.5);
        assertEquals("Sword", item.getName());
        assertEquals(100, item.getValue());
        assertEquals(5.5, item.getWeight());

        // Test setters
        item.setName("Axe");
        item.setValue(150);
        assertEquals("Axe", item.getName());
        assertEquals(150, item.getValue());
        // Weight has no setter, so confirm it's unchanged
        assertEquals(5.5, item.getWeight());
        assertEquals("Axe (hodnota: 150 korun, váha: 5.5kg)", item.toString());
    }
}
