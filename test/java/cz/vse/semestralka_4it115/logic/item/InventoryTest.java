// File: logic.item.InventoryTest.java
package cz.vse.semestralka_4it115.logic.item;

import cz.vse.semestralka_4it115.logic.item.Inventory;
import cz.vse.semestralka_4it115.logic.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private Inventory defaultInventory;
    private Inventory customInventory;
    private Item lightItem;
    private Item heavyItem;

    @BeforeEach
    void setUp() {
        defaultInventory = new Inventory();            // maxWeight defaults to 20
        customInventory = new Inventory(50.0);         // maxWeight set to 50
        lightItem = new Item("Feather", 1, 0.5);
        heavyItem = new Item("Rock", 30, 60.0);
    }

    @Test
    void testDefaultMaxWeight() {
        assertEquals(20.0, defaultInventory.getMaxWeight());
        assertEquals(0.0, defaultInventory.getCurrentWeight());
        assertTrue(defaultInventory.getItems().isEmpty());
    }

    @Test
    void testCustomMaxWeightBelowThreshold() {
        // Passing below or equal to 20 should default to 20, per constructor contract
        Inventory inv = new Inventory(10.0);
        assertEquals(20.0, inv.getMaxWeight());
    }

    @Test
    void testCustomMaxWeightAboveThreshold() {
        assertEquals(50.0, customInventory.getMaxWeight());
    }

    @Test
    void testAddItemWithinLimit() throws Exception {
        defaultInventory.addItem(lightItem);
        assertFalse(defaultInventory.getItems().isEmpty());
        assertEquals(0.5, defaultInventory.getCurrentWeight(), 0.0001);
        assertEquals(lightItem, defaultInventory.getItems().get(0));
    }

    @Test
    void testAddItemExceedsLimit() {
        Exception exception = assertThrows(Exception.class, () -> {
            defaultInventory.addItem(heavyItem);
        });
        String msg = exception.getMessage();
        assertTrue(msg.contains("překračuje maximální hmotnostní limit"));
        assertTrue(defaultInventory.getItems().isEmpty());
    }

    @Test
    void testDropItem() throws Exception {
        defaultInventory.addItem(lightItem);
        assertEquals(0.5, defaultInventory.getCurrentWeight(), 0.0001);
        defaultInventory.dropItem(lightItem);
        assertEquals(0.0, defaultInventory.getCurrentWeight(), 0.0001);
        assertTrue(defaultInventory.getItems().isEmpty());
    }

    @Test
    void testToStringEmpty() {
        String expected = "Inventory:\n";
        assertEquals(expected, defaultInventory.toString());
    }

    @Test
    void testToStringWithItems() throws Exception {
        defaultInventory.addItem(lightItem);
        defaultInventory.addItem(new Item("Stone", 5, 1.0)); // new total weight = 1.5
        String output = defaultInventory.toString();
        assertTrue(output.contains("1. Feather"));
        assertTrue(output.contains("2. Stone"));
    }

    @Test
    void testCurrentWeightAfterMultipleAdds() throws Exception {
        defaultInventory.addItem(new Item("Pecet", 1, 0.1));
        defaultInventory.addItem(new Item("Pivo", 1, 1.0));
        defaultInventory.addItem(new Item("Pivo", 1, 1.0));
        defaultInventory.addItem(new Item("Klic", 1, 0.1));

        assertEquals(2.2, defaultInventory.getCurrentWeight(), 0.0001);
    }
}
