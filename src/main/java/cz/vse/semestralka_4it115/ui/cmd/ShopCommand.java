package cz.vse.semestralka_4it115.ui.cmd;

import cz.vse.semestralka_4it115.ui.game.ShopUI;

/**
 * Represents the shop command to initiate trading with an NPC.
 * <p>
 * Parses the NPC identifier and starts a shopping session via ShopUI, allowing
 * the player to buy or sell items with a dealer in the current room.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-04-03
 */
public class ShopCommand implements Command {
    private String osoba;

    /**
     * Constructs a ShopCommand for the given NPC.
     *
     * @param osoba the name or index of the NPC to trade with
     */
    public ShopCommand(String osoba) {
        this.osoba = osoba;
    }

    /**
     * Executes the shop command by invoking ShopUI.start with the NPC identifier.
     *
     * @throws Exception if the dealer cannot be found or an error occurs during shopping
     */
    @Override
    public void execute() throws Exception {
        ShopUI.start(osoba);
    }
}
