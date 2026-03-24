// File: ui.game.GameTest.java
package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.item.Item;
import cz.vse.semestralka_4it115.logic.item.Weapon;
import cz.vse.semestralka_4it115.logic.space.Map;
import cz.vse.semestralka_4it115.logic.space.Room;
import cz.vse.semestralka_4it115.ui.game.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests core game operations such as movement, inventory lookup, and room state.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
class GameTest {

    private Game game;
    private Room room1;
    private Room room2;
    private Weapon sword;

    @BeforeEach
    void setUp() throws Exception {
        game = new Game();
        // Create two rooms and link them
        room1 = new Room(1, "Hall", "A grand hall");
        room2 = new Room(2, "Chamber", "A dark chamber");
        room1.addExit(2);
        room2.addExit(1);

        // Set up map and assign current room
        Map map = new Map();
        map.addRoom(room1);
        map.addRoom(room2);
        map.setCurrentRoom(room1);
        game.setMap(map);

        // Create a weapon and give to player's inventory
        sword = new Weapon("Sword", 100, 5.0, 20.0);
        game.getPlayer().getInventory().addItem(sword);
        game.getPlayer().setStrength(5.0);
    }


    @Test
    void testGoToNextRoomByName() throws Exception {
        // Move back to room1 first
        game.setCurrentRoom(room2);
        // Use the beginning of the room name "Ha" to move to Hall
        game.goToNextRoom("Ha");
        assertEquals(room1, game.getCurrentRoom());
    }




    @Test
    void testSearchInInventoryNotFoundReturnsNull() {
        Item notFound = game.searchInInventory("Bow");
        assertNull(notFound);
    }

    @Test
    void testGetAndSetPlayer() {
        Person newPlayer = new Person();
        newPlayer.setName("Tester");
        game.setPlayer(newPlayer);
        assertEquals("Tester", game.getPlayer().getName());
    }

    @Test
    void testResetPlayerCreatesFreshPerson() {
        Person original = game.getPlayer();
        original.setName("Original");
        game.resetPlayer("NewName");
        Person reset = game.getPlayer();
        assertNotSame(original, reset);
        assertEquals("NewName", reset.getName());
    }


}
