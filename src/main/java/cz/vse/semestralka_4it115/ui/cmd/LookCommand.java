package cz.vse.semestralka_4it115.ui.cmd;

import cz.vse.semestralka_4it115.serializer.SerHandler;
import cz.vse.semestralka_4it115.ui.game.LookUI;

import java.util.Map;

/**
 * Parses user input for look commands and invokes LookUI to display room information.
 * <p>
 * Accepts actions like exits, items, people, or detail to show relevant information.
 * Loads valid look actions from a serialized map and validates input before executing.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class LookCommand implements Command {
    private String action;

    /**
     * Map of input strings to LookActions enum values, loaded from serialized resource.
     */
    private Map<String, LookActions> commands = SerHandler.loadLookActionsMap("resources\\lookActions.ser");

    /**
     * Constructs a new LookCommand instance with no initial action.
     */
    public LookCommand() {
        this.action = null;
    }

    /**
     * Constructs a new LookCommand instance with action.
     *
     * @param action look action keyword entered by player
     */
    public LookCommand(String action) {
        this.action = action.toLowerCase();
    }

    /**
     * Executes the look command by validating the action and invoking LookUI.
     * <p>
     * If the action is not recognized, throws an IllegalArgumentException.
     *
     * @throws IllegalArgumentException if the action is not recognized
     */
    @Override
    public void execute() {
        Boolean isCommandValid = false;
        if (action != null) {
            if (commands.containsKey(action)) {
                action = commands.get(action).toString().toLowerCase();
                isCommandValid = true;
            }
        } else {
            action = null;
            isCommandValid = true;
        }
        if (isCommandValid) {
            LookUI.start(action);
        } else {
            throw new IllegalArgumentException("Neplatný příkaz.");
        }
    }
}
