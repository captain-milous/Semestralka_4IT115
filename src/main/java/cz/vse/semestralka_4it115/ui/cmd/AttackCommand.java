package cz.vse.semestralka_4it115.ui.cmd;


import cz.vse.semestralka_4it115.ui.game.FightUI;

/**
 * The AttackCommand class handles attack command functionality.
 *
 * Constructs and executes an attack on a specified target in the current room,
 * delegating combat logic to FightUI. Used when the player issues an "attack" command.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class AttackCommand implements Command {
    private String target;

    /**
     * Constructs an AttackCommand for the specified target.
     *
     * @param target the name or index of the character to attack in the current room
     */
    public AttackCommand(String target) {
        this.target = target;
    }


    /**
     * Executes the attack command by initiating combat with the target via FightUI.
     *
     * @throws Exception if the fight cannot be started or an error occurs during combat
     */
    @Override
    public void execute() throws Exception {
        FightUI.start(target);
    }
}
