package cz.vse.semestralka_4it115.ui.cmd;

import static cz.vse.semestralka_4it115.ui.game.GameUI.GH;

/**
 * Represents the move command to change the player's current room.
 * <p>
 * Parses the room input provided by the user and moves the player accordingly
 * by calling the game logic to handle exits, locks, and enemies.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class MoveCommand implements Command {
    String roomInput = null;

    /**
     * Constructs a MoveCommand with the specified room input.
     *
     * @param roomInput the name or index of the room to move to
     */
    public MoveCommand(String roomInput) {
        if (!roomInput.isEmpty()) {
            this.roomInput = roomInput;
        }
    }

    /**
     * Executes the move command by calling goToNextRoom on the game with the provided input.
     *
     * @throws Exception if movement fails due to invalid input, locked rooms, or enemy presence
     */
    @Override
    public void execute() throws Exception {
        GH.game.goToNextRoom(roomInput);
    }
}
