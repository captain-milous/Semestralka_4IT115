// File: logic.item.ArmorTest.java
package cz.vse.semestralka_4it115.logic.item;

import cz.vse.semestralka_4it115.logic.item.Armor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies armor attributes and output formatting.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
class ArmorTest {

    @Test
    void testConstructorAndGetDefense() {
        Armor armor = new Armor("Chainmail", 200, 15.0, 8.0);
        assertEquals("Chainmail", armor.getName());
        assertEquals(200, armor.getValue());
        assertEquals(15.0, armor.getWeight());
        assertEquals(8.0, armor.getDefense());
    }

    @Test
    void testToString() {
        Armor armor = new Armor("Plate", 300, 20.0, 12.5);
        String expected = "Plate (hodnota: 300 korun, váha: 20.0kg, obrana: 12.5)";
        assertEquals(expected, armor.toString());
    }
}
