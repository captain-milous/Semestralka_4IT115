package cz.vse.semestralka_4it115.main;

import cz.vse.semestralka_4it115.ui.cmd.AttackCommand;
import cz.vse.semestralka_4it115.ui.cmd.InventoryCommand;
import cz.vse.semestralka_4it115.ui.cmd.MoveCommand;
import cz.vse.semestralka_4it115.ui.cmd.ShopCommand;
import cz.vse.semestralka_4it115.ui.cmd.TalkCommand;
import cz.vse.semestralka_4it115.ui.game.LookUI;
import javafx.application.Platform;

/**
 * GUI-specific command executor independent of serialized command maps.
 * Keeps command behavior aligned with text mode while remaining reliable in JavaFX runtime.
 */
public class GuiCommandExecutor {
    private final Runnable showMapAction;

    /**
     * Creates executor with no GUI map action.
     */
    public GuiCommandExecutor() {
        this(null);
    }

    /**
     * Creates executor with optional map window action for "prohledat mapu".
     *
     * @param showMapAction callback that opens enlarged map window
     */
    public GuiCommandExecutor(Runnable showMapAction) {
        this.showMapAction = showMapAction;
    }

    /**
     * Executes one user command from GUI input.
     *
     * @param rawInput full command entered by user
     * @throws Exception if parsing or execution fails
     */
    public void execute(String rawInput) throws Exception {
        if (rawInput == null || rawInput.isBlank()) {
            return;
        }

        String[] tokens = rawInput.trim().toLowerCase().split("\\s+");
        String cmd = tokens[0];

        switch (cmd) {
            case "help":
                break;

            case "exit":
                System.out.println("Ukončuji hru. Nashledanou!");
                Platform.exit();
                break;

            case "jdi":
                if (tokens.length < 2) {
                    throw new IllegalArgumentException("Pro příkaz 'jdi' je potřeba zadat směr.");
                }
                new MoveCommand(tokens[1]).execute();
                break;

            case "utok":
                if (tokens.length < 2) {
                    throw new IllegalArgumentException("Pro příkaz 'utok' je potřeba zadat osobu.");
                }
                new AttackCommand(tokens[1]).execute();
                break;

            case "batoh":
                if (tokens.length < 2) {
                    throw new IllegalArgumentException("Pro příkaz 'batoh' je potřeba zadat akci.");
                }
                if (tokens.length == 2) {
                    new InventoryCommand(tokens[1]).execute();
                } else {
                    new InventoryCommand(tokens[1], tokens[2]).execute();
                }
                break;

            case "prohledat":
                executeLook(tokens);
                break;

            case "nakup":
                if (tokens.length < 2) {
                    throw new IllegalArgumentException("Pro příkaz 'nakup' je potřeba zadat osobu.");
                }
                new ShopCommand(tokens[1]).execute();
                break;

            case "mluvit":
                if (tokens.length < 2) {
                    throw new IllegalArgumentException("Pro příkaz 'mluvit' je potřeba zadat osobu.");
                }
                new TalkCommand(tokens[1]).execute();
                break;

            default:
                throw new IllegalArgumentException("Neznámý příkaz. Napiš 'help' pro nápovědu.");
        }
    }

    /**
     * Executes look command variants directly.
     *
     * @param tokens parsed command tokens
     */
    private void executeLook(String[] tokens) {
        if (tokens.length == 1) {
            LookUI.start(null);
            return;
        }

        String action = tokens[1];
        switch (action) {
            case "mapu":
            case "mapa":
            case "map":
                if (showMapAction != null) {
                    showMapAction.run();
                } else {
                    throw new IllegalArgumentException("Mapu nelze v tomto rozhraní otevřít.");
                }
                break;
            case "podrobny":
            case "detail":
                LookUI.start("detail");
                break;
            case "vychody":
            case "exits":
                LookUI.start("exits");
                break;
            case "veci":
            case "items":
                LookUI.start("items");
                break;
            case "lide":
            case "people":
                LookUI.start("people");
                break;
            default:
                throw new IllegalArgumentException("Neplatný příkaz 'prohledat'.");
        }
    }
}
