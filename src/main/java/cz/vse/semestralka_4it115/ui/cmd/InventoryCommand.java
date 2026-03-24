package cz.vse.semestralka_4it115.ui.cmd;

import cz.vse.semestralka_4it115.ui.game.InventoryUI;

/**
 * Represents the inventory command to manage the player's inventory.
 *
 * Parses inventory actions and item names, and delegates execution to InventoryUI
 * to perform operations such as listing, taking, dropping, or inspecting items.
 * The command is constructed with an action (e.g., list, take) and an optional item name.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class InventoryCommand implements Command {
    //region Fields
    private String action;
    private String itemName;
    //endregion

    /**
     * Constructs an InventoryCommand with the specified action.
     *
     * @param action inventory action keyword
     */
    public InventoryCommand(String action) {
        this.action = action;
    }

    /**
     * Constructs an InventoryCommand with the specified action and item name.
     *
     * @param action   the inventory action to perform (e.g., list, take, drop, inspect)
     * @param itemName the name or identifier of the item to affect (optional, depending on action)
     */
    public InventoryCommand(String action, String itemName) {
        this.action = action;
        this.itemName = itemName;
    }
    //endregion

    /**
     * Executes the inventory command by invoking InventoryUI.start with the action and item name.
     *
     * @throws Exception if the inventory operation fails or the input is invalid
     */
    @Override
    public void execute() throws Exception {
        InventoryUI.start(action, itemName);
    }
}
