package cz.vse.semestralka_4it115.serializer;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.item.Armor;
import cz.vse.semestralka_4it115.logic.item.Food;
import cz.vse.semestralka_4it115.logic.item.Item;
import cz.vse.semestralka_4it115.logic.item.Weapon;
import cz.vse.semestralka_4it115.logic.space.Map;
import cz.vse.semestralka_4it115.logic.space.Room;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Validates JSON map loading and concrete item deserialization.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
class JsonHandlerTest {

    @Test
    void testLoadMapDeserializesConcreteItemTypes() throws IOException {
        String path = Path.of("maps", "HARD", "H1.json").toString();
        Map map = JsonHandler.loadMap(path);

        List<Item> allItems = new ArrayList<>();
        for (Room room : map.getLayout()) {
            allItems.addAll(room.getItems());
            for (Person person : room.getOtherPeople()) {
                allItems.addAll(person.getInventory().getItems());
            }
        }

        assertFalse(allItems.isEmpty());
        assertTrue(allItems.stream().anyMatch(item -> item instanceof Food));
        assertTrue(allItems.stream().anyMatch(item -> item instanceof Weapon));
        assertTrue(allItems.stream().anyMatch(item -> item instanceof Armor));
    }
}
