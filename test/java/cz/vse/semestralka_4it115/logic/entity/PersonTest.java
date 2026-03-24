// File: logic.entity.PersonTest.java
package cz.vse.semestralka_4it115.logic.entity;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.item.Armor;
import cz.vse.semestralka_4it115.logic.item.Weapon;
import cz.vse.semestralka_4it115.logic.item.Food;
import cz.vse.semestralka_4it115.logic.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests combat stats, inventory effects, and health transitions of {@link Person}.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
class PersonTest {

    private Person person;
    private Weapon sword;
    private Armor armor;
    private Food apple;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setName("Hero");
        sword = new Weapon("Sword", 150, 5.0, 12.0);
        armor = new Armor("Chainmail", 200, 15.0, 8.0);
        apple = new Food("Apple", 5, 0.2, 10.0);
    }

    @Test
    void testHealthManagement() {
        person.setMaxHealth(100.0);
        person.setHealth(80.0);
        assertEquals(80.0, person.getHealth());
        person.takeDamage(30.0);
        assertEquals(50.0, person.getHealth());
        assertTrue(person.isAlive());
        person.takeDamage(100.0);
        assertFalse(person.isAlive());
    }

}
