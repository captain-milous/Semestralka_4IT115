// src/ui/game/InventoryUI.java
package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.item.Item;
import cz.vse.semestralka_4it115.logic.space.Room;

import java.util.List;

import static cz.vse.semestralka_4it115.ui.game.GameUI.GH;

/**
 * Třída InventoryUI zajišťuje obsluhu příkazů batohu: výpis obsahu, sebrání, odhození a prohlédnutí předmětu.
 * Metoda start() přijímá akci a volitelně název předmětu a podle toho volá příslušné privátní metody.
 * Výstupy jsou v češtině a chybné stavy se řeší házením výjimek.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-04-03
 */
public class InventoryUI {

    /**
     * Vstupní metoda pro obsluhu příkazů batohu.
     *
     * @param action   akce nad batohem ("list", "take", "drop", "inspect")
     * @param itemName jméno předmětu (pouze pro "take", "drop" a "inspect"; pro "list" bývá null)
     * @throws Exception               v případě obecné chyby (např. nelze přidat kvůli váze, předmět neexistuje apod.)
     * @throws IllegalArgumentException pokud je akce nesprávná nebo chybí název předmětu tam, kde je potřeba
     */
    public static void start(String action, String itemName) throws Exception {
        if (action == null) {
            throw new IllegalArgumentException("Akce batohu není zadána.");
        }

        switch (action.toLowerCase()) {
            case "list":
                listItems();
                break;

            case "vezmi":
                if (itemName == null || itemName.isBlank()) {
                    throw new IllegalArgumentException("Zadejte název předmětu, který chcete sebrat.");
                }
                takeItem(itemName);
                break;

            case "zahod":
                if (itemName == null || itemName.isBlank()) {
                    throw new IllegalArgumentException("Zadejte název předmětu, který chcete odhodit.");
                }
                dropItem(itemName);
                break;

            case "info":
                if (itemName == null || itemName.isBlank()) {
                    throw new IllegalArgumentException("Zadejte název předmětu, který chcete prohlédnout.");
                }
                inspectItem(itemName);
                break;

            default:
                throw new IllegalArgumentException("Neplatná akce batohu: " + action);
        }
    }

    /**
     * Vypíše seznam předmětů v batohu hráče.
     */
    private static void listItems() {
        Person player = GH.game.getPlayer();
        List<Item> items = player.getInventory().getItems();

        if (items == null || items.isEmpty()) {
            System.out.println("V batohu nemáte žádné předměty.");
        } else {
            System.out.println("Předměty v batohu:");
            int i = 1;
            for (Item item : items) {
                System.out.println("  " + i + ". " + item.toString());
                i++;
            }
        }
    }

    /**
     * Sebere předmět z aktuální místnosti do batohu hráče.
     *
     * @param itemName jméno předmětu, který se má sebrat
     * @throws Exception pokud předmět v místnosti není nebo dojde k chybě při přidání do batohu
     */
    private static void takeItem(String itemName) throws Exception {
        Room currentRoom = GH.game.getCurrentRoom();
        List<Item> roomItems = currentRoom.getItems();
        itemName = itemName.toLowerCase();

        if (roomItems == null || roomItems.isEmpty()) {
            throw new Exception("V místnosti nejsou žádné předměty k sebrání.");
        }

        Item found = null;
        for (Item item : roomItems) {
            if (itemName.startsWith(item.getName().toLowerCase())) {
                found = item;
                break;
            }
        }

        if (found == null) {
            throw new Exception("Předmět '" + itemName + "' není v místnosti.");
        }

        // 1) Nejprve zkus přidat do inventáře hráče (může vyhodit výjimku při překročení váhového limitu)
        GH.game.getPlayer().addItemToInventory(found);

        // 2) Z místnosti odebírej až po úspěšném přidání do inventáře
        currentRoom.removeItem(found);

        System.out.println("Sebral jste " + found.getName() + ".");
    }

    /**
     * Odhodí předmět z batohu hráče a vloží ho do aktuální místnosti.
     *
     * @param itemName jméno předmětu, který se má odhodit
     * @throws Exception pokud předmět v batohu není nebo dojde k chybě při vrácení do místnosti
     */
    private static void dropItem(String itemName) throws Exception {
        Person player = GH.game.getPlayer();
        List<Item> items = player.getInventory().getItems();
        itemName = itemName.toLowerCase();

        if (items == null || items.isEmpty()) {
            throw new Exception("V batohu nemáte žádné předměty k odhození.");
        }

        Item found = null;
        for (Item item : items) {
            if (itemName.startsWith(item.getName().toLowerCase())) {
                found = item;
                break;
            }
        }

        if (found == null) {
            throw new Exception("Předmět '" + itemName + "' nemáte v batohu.");
        }

        // 1) Odeber z batohu hráče (může vyhodit výjimku, pokud se něco pokazí)
        player.dropItemFromInventory(found);

        // 2) Přidej do místnosti
        GH.game.getCurrentRoom().addItem(found);

        System.out.println("Odhodil jste " + found.getName() + ".");
    }


    /**
     * Zobrazí detailní informace o předmětu v inventáři hráče.
     *
     * @param itemName název předmětu, který má hráč prohlédnout
     */
    private static void inspectItem(String itemName) {
        itemName = itemName.toLowerCase();
        Item found = GH.game.searchInInventory(itemName);

        if (found == null) {
            System.out.println("Předmět '" + itemName + "' nemáte v inventáři.\n");
            return;
        }

        System.out.println("Detail předmětu:\n" + found.toString() + "\n");
    }
}
