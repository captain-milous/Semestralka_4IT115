// File: logic.space.MapTest.java
package cz.vse.semestralka_4it115.logic.space;

import cz.vse.semestralka_4it115.logic.space.Map;
import cz.vse.semestralka_4it115.logic.space.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    private Map map;
    private Room room1;
    private Room room2;

    @BeforeEach
    void setUp() {
        map = new Map();
        room1 = new Room(1, "Hall", "Large hall");
        room2 = new Room(2, "Kitchen", "Food preparation area");
    }

    @Test
    void testAddRoomDuplicateIdThrows() {
        map.addRoom(room1);
        Room duplicate = new Room(1, "Empty", "Duplicate room");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            map.addRoom(duplicate);
        });
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testSetAndGetCurrentRoom() {
        map.setCurrentRoom(room1);
        assertEquals(room1, map.getCurrentRoom());
    }

}
