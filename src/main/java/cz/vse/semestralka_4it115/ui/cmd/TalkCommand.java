package cz.vse.semestralka_4it115.ui.cmd;

import cz.vse.semestralka_4it115.ui.game.TalkUI;

/**
 * Represents the talk command to converse with an NPC.
 * <p>
 * Parses the NPC identifier and initiates a dialogue session via TalkUI,
 * allowing the player to engage in conversations with characters in the room.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class TalkCommand implements Command {
    private String osoba;

    /**
     * Constructs a TalkCommand for the specified NPC.
     *
     * @param osoba the name or index of the NPC to talk to
     */
    public TalkCommand(String osoba) {
        this.osoba = osoba;
    }

    /**
     * Executes the talk command by invoking TalkUI.start with the NPC identifier.
     *
     * @throws Exception if the NPC cannot be found or an error occurs during conversation
     */
    @Override
    public void execute() throws Exception {
        TalkUI.start(osoba);
    }
}
