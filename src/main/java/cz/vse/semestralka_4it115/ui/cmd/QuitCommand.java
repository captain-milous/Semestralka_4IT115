package cz.vse.semestralka_4it115.ui.cmd;

/**
 * Represents the command to quit the game, exiting the application.
 * When executed, it prints a farewell message and terminates the program.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class QuitCommand implements Command {
    /**
     * Constructs a new QuitCommand instance.
     */
    public QuitCommand() {

    }

    /**
     * Executes the quit command by printing a farewell message and terminating the program.
     */
    @Override
    public void execute() {
        System.out.println("Ukončuji hru. Nashledanou!");
        System.exit(0);
    }
}
