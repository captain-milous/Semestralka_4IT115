// File: logic.item.WeaponTest.java
package cz.vse.semestralka_4it115.logic.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

    @Test
    void testConstructorAndGetDamage() {
        Weapon weapon = new Weapon("Hammer", 250, 10.0, 15.0);
        assertEquals("Hammer", weapon.getName());
        assertEquals(250, weapon.getValue());
        assertEquals(10.0, weapon.getWeight());
        assertEquals(15.0, weapon.getDamage());
    }

    @Test
    void testToString() {
        Weapon weapon = new Weapon("Sword", 150, 7.5, 10.0);
        String expected = "Sword (hodnota: 150 korun, váha: 7.5kg, poškození: 10.0)";
        assertEquals(expected, weapon.toString());
    }
}
