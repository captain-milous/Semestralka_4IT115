package cz.vse.semestralka_4it115.ui.game;

import static cz.vse.semestralka_4it115.ui.game.GameUI.GH;

/**
 * Handles look commands to display information about the current room, such as exits, items, or people present.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class LookUI {
    /**
     * Executes a look action based on the provided parameter, displaying room details or throwing an exception for invalid commands.
     *
     * @param action the look action to perform ("exits", "items", or "people")
     * @throws IllegalArgumentException if the action is not recognized
     */
    public static void start(String action) throws IllegalArgumentException {
        System.out.println("Nacházíš se v " + GH.game.getCurrentRoom().getName());
        switch (action) {
            case null:
                System.out.println(GH.game.getCurrentRoom().getDescription() + "\n");
                break;
            case "detail":
                System.out.println("- " + GH.game.getCurrentRoom().getDescription() + "\n");
                System.out.println("- " + GH.game.getCurrentRoom().peopleToString() + "\n");
                System.out.println("- " + GH.game.getCurrentRoom().itemsToString() + "\n");
                System.out.println("- " + GH.game.currentExitsToString() + "\n");
                break;
            case "exits":
                System.out.println(GH.game.currentExitsToString() + "\n");
                break;
            case "items":
                System.out.println(GH.game.getCurrentRoom().itemsToString() + "\n");
                break;
            case "people":
                System.out.println(GH.game.getCurrentRoom().peopleToString() + "\n");
                break;
            default:
                throw new IllegalArgumentException("Neplatný příkaz.");
        }
    }
}
