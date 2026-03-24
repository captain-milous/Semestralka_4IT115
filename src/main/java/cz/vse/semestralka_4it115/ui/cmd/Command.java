package cz.vse.semestralka_4it115.ui.cmd;

/**
 * Defines a generic command that can be executed within the game.
 * Implementations perform specific actions when executed.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public interface Command {
    /**
     * Executes this command.
     *
     * @throws Exception if an error occurs during command execution
     */
    void execute() throws Exception;
}
