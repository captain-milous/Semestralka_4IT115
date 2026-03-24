package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.item.Food;
import cz.vse.semestralka_4it115.logic.item.Item;
import cz.vse.semestralka_4it115.logic.space.Room;

import java.util.List;

import static cz.vse.semestralka_4it115.ui.game.GameUI.GH;

/**
 * Implements inventory commands for listing, taking, dropping, inspecting, and using items.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class InventoryUI {

    /**
     * Entry point for inventory command handling.
     *
     * @param action inventory action
     * @param itemName item name for actions that need it
     * @throws Exception when command cannot be completed
     */
    public static void start(String action, String itemName) throws Exception {
        if (action == null) {
            throw new IllegalArgumentException("Akce batohu neni zadana.");
        }

        switch (action.toLowerCase()) {
            case "list":
                listItems();
                break;

            case "vezmi":
                if (itemName == null || itemName.isBlank()) {
                    throw new IllegalArgumentException("Zadejte nazev predmetu, ktery chcete sebrat.");
                }
                takeItem(itemName);
                break;

            case "zahod":
                if (itemName == null || itemName.isBlank()) {
                    throw new IllegalArgumentException("Zadejte nazev predmetu, ktery chcete odhodit.");
                }
                dropItem(itemName);
                break;

            case "info":
                if (itemName == null || itemName.isBlank()) {
                    throw new IllegalArgumentException("Zadejte nazev predmetu, ktery chcete prohlednout.");
                }
                inspectItem(itemName);
                break;

            case "pouzij":
            case "use":
                if (itemName == null || itemName.isBlank()) {
                    throw new IllegalArgumentException("Zadejte nazev predmetu, ktery chcete pouzit.");
                }
                useItem(itemName);
                break;

            default:
                throw new IllegalArgumentException("Neplatna akce batohu: " + action);
        }
    }

    /**
     * Prints player's inventory to output.
     */
    private static void listItems() {
        Person player = GH.game.getPlayer();
        List<Item> items = player.getInventory().getItems();

        if (items == null || items.isEmpty()) {
            System.out.println("V batohu nemate zadne predmety.");
            return;
        }

        System.out.println("Predmety v batohu:");
        int i = 1;
        for (Item item : items) {
            System.out.println("  " + i + ". " + item);
            i++;
        }
    }

    /**
     * Takes item from current room.
     *
     * @param itemName item name
     * @throws Exception when item cannot be taken
     */
    private static void takeItem(String itemName) throws Exception {
        Room currentRoom = GH.game.getCurrentRoom();
        List<Item> roomItems = currentRoom.getItems();
        String loweredName = itemName.toLowerCase();

        if (roomItems == null || roomItems.isEmpty()) {
            throw new Exception("V mistnosti nejsou zadne predmety k sebrani.");
        }

        Item found = null;
        for (Item item : roomItems) {
            if (loweredName.startsWith(item.getName().toLowerCase())) {
                found = item;
                break;
            }
        }

        if (found == null) {
            throw new Exception("Predmet '" + itemName + "' neni v mistnosti.");
        }

        GH.game.getPlayer().addItemToInventory(found);
        currentRoom.removeItem(found);
        System.out.println("Sebral jste " + found.getName() + ".");
    }

    /**
     * Drops item from inventory to current room.
     *
     * @param itemName item name
     * @throws Exception when item cannot be dropped
     */
    private static void dropItem(String itemName) throws Exception {
        Person player = GH.game.getPlayer();
        List<Item> items = player.getInventory().getItems();
        String loweredName = itemName.toLowerCase();

        if (items == null || items.isEmpty()) {
            throw new Exception("V batohu nemate zadne predmety k odhozeni.");
        }

        Item found = null;
        for (Item item : items) {
            if (loweredName.startsWith(item.getName().toLowerCase())) {
                found = item;
                break;
            }
        }

        if (found == null) {
            throw new Exception("Predmet '" + itemName + "' nemate v batohu.");
        }

        player.dropItemFromInventory(found);
        GH.game.getCurrentRoom().addItem(found);
        System.out.println("Odhodil jste " + found.getName() + ".");
    }

    /**
     * Prints item details from player's inventory.
     *
     * @param itemName item name
     */
    private static void inspectItem(String itemName) {
        Item found = GH.game.searchInInventory(itemName.toLowerCase());

        if (found == null) {
            System.out.println("Predmet '" + itemName + "' nemate v inventari.\n");
            return;
        }

        System.out.println("Detail predmetu:\n" + found + "\n");
    }

    /**
     * Uses one item from inventory.
     * Currently supports consumable food items.
     *
     * @param itemName item name
     * @throws Exception when item is missing or not usable
     */
    private static void useItem(String itemName) throws Exception {
        Person player = GH.game.getPlayer();
        Item found = GH.game.searchInInventory(itemName.toLowerCase());

        if (found == null) {
            throw new Exception("Predmet '" + itemName + "' nemate v batohu.");
        }

        if (found instanceof Food food) {
            double healthBefore = player.getHealth();
            player.addHealth(food.getHeal());
            player.dropItemFromInventory(found);
            double healed = player.getHealth() - healthBefore;
            System.out.println("Pouzil jste " + food.getName() + " a obnovil " + String.format("%.1f", healed) + " zdravi.");
            return;
        }

        throw new Exception("Predmet '" + found.getName() + "' nelze pouzit.");
    }
}
