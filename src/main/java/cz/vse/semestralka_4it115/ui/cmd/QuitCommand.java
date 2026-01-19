package cz.vse.semestralka_4it115.ui.cmd;

/**
 * Represents the command to quit the game, exiting the application.
 * When executed, it prints a farewell message and terminates the program.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-04-03
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
