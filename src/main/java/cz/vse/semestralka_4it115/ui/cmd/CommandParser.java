package cz.vse.semestralka_4it115.ui.cmd;

import cz.vse.semestralka_4it115.serializer.SerHandler;
import cz.vse.semestralka_4it115.ui.game.GameUI;

import java.util.Map;

/**
 * Parses user input into game commands, mapping input strings to command enum values
 * and returning corresponding Command objects. Handles unknown commands by prompting help.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class CommandParser {
    private Map<String, CMD> commands = SerHandler.loadCommandsMap("resources\\commands.ser");

    /**
     * Parses the user input string into a corresponding Command object.
     * Splits the input by whitespace to determine the command keyword and parameters,
     * matches the keyword against the available commands map, and constructs the appropriate
     * Command implementation. If the command is unrecognized or parameters are missing,
     * displays an error message and returns null.
     *
     * @param input the raw input string entered by the user
     * @return a Command object corresponding to the parsed input, or null if the command is invalid
     * @throws Exception if an error occurs during command parsing or command creation
     */
    public Command parseCommand(String input) throws Exception {
        String[] tokens = input.trim().split("\\s+");
        String commandKeyword = tokens[0].toLowerCase();
        int index = 0;

        if (commands.containsKey(commandKeyword)) {
            CMD cmd = commands.get(commandKeyword);

            switch (cmd) {
                case CMD.jdi:
                    if (tokens.length == 2) {
                        return new MoveCommand(tokens[1]);
                    } else {
                        System.out.println("Pro příkaz 'jdi' je potřeba zadat směr.");
                        GameUI.wait(2000);
                        return null;
                    }

                case CMD.utok:
                    if (tokens.length == 2) {
                        return new AttackCommand(tokens[1]);
                    } else {
                        System.out.println("Pro příkaz 'utok' je potřeba zadat osobu, do které budete útočit.");
                        GameUI.wait(2000);
                        return null;
                    }

                case CMD.batoh:
                    if (tokens.length == 2) {
                        return new InventoryCommand(tokens[1]);
                    } else if (tokens.length == 3) {
                        return new InventoryCommand(tokens[1], tokens[2]);
                    } else {
                        System.out.println("Pro příkaz 'batoh' je potřeba zadat...");
                        GameUI.wait(2000);
                        return null;
                    }

                case CMD.prohledat:
                    if (tokens.length == 1) {
                        return new LookCommand();
                    }
                    if (tokens.length == 2) {
                        return new LookCommand(tokens[1]);
                    } else {
                        return new LookCommand();
                    }

                case CMD.mluvit:
                    if (tokens.length == 2) {
                        return new TalkCommand(tokens[1]);
                    } else {
                        System.out.println("Pro příkaz 'mluvit' je potřeba zadat osobu se kterou chcete mluvit.");
                        GameUI.wait(2000);
                        return null;
                    }

                case CMD.nakup:
                    if (tokens.length == 2) {
                        return new ShopCommand(tokens[1]);
                    } else {
                        System.out.println("Pro příkaz 'nakup' je potřeba zadat osobu se kterou chcete obchodovat.");
                        GameUI.wait(2000);
                        return null;
                    }

                case CMD.help:
                    return new HelpCommand();

                case CMD.exit:
                    return new QuitCommand();

                default:
                    System.out.println("Neznámý příkaz. Napište 'help' pro nápovědu.");
                    GameUI.wait(2000);
                    return null;
            }

        } else {
            System.out.println("Neznámý příkaz. Napište 'help' pro nápovědu.");
            GameUI.wait(2000);
            return null;
        }
    }

}
