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
 * @version 1.0.0
 * @since 2026-03-23
 */
public enum InventoryAction {
    LIST, TAKE, DROP, INSPECT
}
