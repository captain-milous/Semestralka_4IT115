// File: logic.space.RoomTest.java
package cz.vse.semestralka_4it115.logic.space;

import cz.vse.semestralka_4it115.logic.item.Item;
import cz.vse.semestralka_4it115.logic.item.Weapon;
import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.space.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests room state changes, collections, and enemy handling behavior.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
class RoomTest {

    private Room room;
    private Item sword;
    private Person npc;

    @BeforeEach
    void setUp() {
        room = new Room(1, "Hall", "A large hall");
        sword = new Weapon("Sword", 150, 7.0, 10.0);
        npc = new Person();
        npc.setName("Guard");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(1, room.getId());
        assertEquals("Hall", room.getName());
        assertEquals("A large hall", room.getDescription());
        assertFalse(room.isLocked());
        assertTrue(room.getItems().isEmpty());
        assertTrue(room.getExits().isEmpty());
        assertTrue(room.getOtherPeople().isEmpty());
    }

    @Test
    void testLockAndUnlock() {
        room.setLocked(true);
        assertTrue(room.isLocked());
        room.setLocked(false);
        assertFalse(room.isLocked());
    }

    @Test
    void testAddExitAndGetExits() {
        room.addExit(2);
        room.addExit(3);
        List<Integer> exits = room.getExits();
        assertEquals(2, exits.size());
        assertTrue(exits.contains(2));
        assertTrue(exits.contains(3));
    }

    @Test
    void testAddAndRemoveItem() throws Exception {
        room.addItem(sword);
        assertFalse(room.getItems().isEmpty());
        assertEquals(sword, room.getItems().get(0));

        room.removeItem(sword);
        assertTrue(room.getItems().isEmpty());
    }

    @Test
    void testAddAndRemovePerson() {
        room.setOtherPeople(List.of(npc));
        assertEquals(1, room.getOtherPeople().size());
        assertEquals("Guard", room.getOtherPeople().get(0).getName());

        // Removing via setting new list without npc
        room.setOtherPeople(List.of());
        assertTrue(room.getOtherPeople().isEmpty());
    }

    @Test
    void testToStringIncludesLockStatus() {
        room.setLocked(true);
        String output = room.toString();
        assertTrue(output.contains("(Zamčená)"));
        assertTrue(output.contains("Hall"));
    }
}
