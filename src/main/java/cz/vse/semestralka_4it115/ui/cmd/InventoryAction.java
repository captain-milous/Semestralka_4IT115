package cz.vse.semestralka_4it115.ui.cmd;

/**
 * Enum representing possible inventory actions a player can perform.
 * LIST to view current items, TAKE to pick up an item, DROP to remove an item,
 * and INSPECT to examine an item in detail.
 *
 * This enum is used by InventoryCommand to determine the requested operation
 * on the player's inventory.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-04-03
 */
public enum InventoryAction {
    LIST, TAKE, DROP, INSPECT
}
