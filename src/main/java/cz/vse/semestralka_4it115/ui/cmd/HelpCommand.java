package cz.vse.semestralka_4it115.ui.cmd;

import cz.vse.semestralka_4it115.serializer.TxtHandler;

import java.util.Scanner;

/**
 * The HelpCommand class handles helpcommand functionality.
 * <p>
 * It loads the help text from a resource file and displays it to the user when executed.
 * Used to provide instructions or available commands within the game.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class HelpCommand implements Command {
    private static String HELP_FILE = "resources\\help.txt";

    /**
     * Constructs a new HelpCommand instance.
     */
    public HelpCommand() {

    }

    /**
     * Loads and displays the help text to the user.
     */
    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        String helpText = new TxtHandler().loadTXT(HELP_FILE);
        System.out.println(helpText);
        sc.nextLine();
    }
}
