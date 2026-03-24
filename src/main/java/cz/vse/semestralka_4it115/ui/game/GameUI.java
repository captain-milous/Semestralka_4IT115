package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.exception.DeathException;
import cz.vse.semestralka_4it115.exception.WinException;
import cz.vse.semestralka_4it115.logic.space.Difficulty;
import cz.vse.semestralka_4it115.score.BestResultsService;
import cz.vse.semestralka_4it115.serializer.TxtHandler;
import cz.vse.semestralka_4it115.ui.cmd.Command;
import cz.vse.semestralka_4it115.ui.cmd.CommandParser;
import java.util.Scanner;

/**
 * Provides the console-based user interface for the game, handling input, output,
 * and the main game loop. Responsible for interactions such as introductions,
 * setting up the player, selecting difficulty, loading maps, and processing commands.
 * Also manages end-of-game scenarios and displays tutorial information.
 * <p>
 * Uses GameHandler to manage game state and TxtHandler to load text resources.
 * </p>
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class GameUI {
    //region static
    /**
     * GameHandler instance used to manage game state and logic.
     */
    public static GameHandler GH = new GameHandler();

    public static CommandParser CP;

    /**
     * String used as a visual separator in console output.
     */
    public static String oddelovac = "——————————————————————————————";

    /**
     * Scanner to read user input from console.
     */
    public static Scanner sc = new Scanner(System.in);

    /**
     * File path for the tutorial text resource.
     */
    private static final String TUTORIAL_FILE = "resources\\tutorial.txt";

    /**
     * File path for the introduction text resource.
     */
    private static final String INTRO_FILE = "resources\\intro.txt";
    private static final BestResultsService BEST_RESULTS_SERVICE = BestResultsService.createDefault();

    //endregion
    //region Constructor

    /**
     * Constructs a new GameUI instance.
     */
    public GameUI() {

    }
    //endregion

    /**
     * Starts the game by displaying the introduction, setting up the player name,
     * selecting difficulty, loading the map, and entering the main play loop.
     */
    public static void start() {
        CP = new CommandParser();
        introduction();
        setUpGamerName();
        setUpDificulty();
        setUpMap();
        play();
    }

    /**
     * Restarts the game by resetting the player, reselecting difficulty, and reloading the map.
     */
    public static void restart() {
        GH.game.resetPlayer(GH.game.getPlayer().getName());
        CP = new CommandParser();
        setUpDificulty();
        setUpMap();
        play();
    }

    /**
     * Displays the introduction text, including game title and author information,
     * and waits for user confirmation to continue.
     */
    private static void introduction() {
        clearConsole();
        System.out.println(oddelovac + "\n");
        System.out.println("Hra: Kraluv posel\nVerze: BETA\nAutor: Milos Tesar\nPozn.: Semestralni práce pro predmet 4IT101\n");
        System.out.println("Stisknete 'Enter' pro pokracovani..\n");
        System.out.println(oddelovac);
        String input = sc.nextLine();
        if (!input.toLowerCase().startsWith("s")) {
            tutorial();
        }
        clearConsole();
    }

    /**
     * Displays the tutorial text and waits for user confirmation to continue.
     */
    private static void tutorial() {
        clearConsole();
        System.out.println(oddelovac + "\n");
        System.out.println("Základní tutoriál: \n");
        try {
            TxtHandler th = new TxtHandler();
            String tutorial = th.loadTXT(TUTORIAL_FILE);
            System.out.println(tutorial);
        } catch (Exception e) {
            System.out.println("Nepodařilo se načíst tutoriál.");
            System.out.println(e.getMessage());
            System.out.println("Hra se ovládá těmito příkazy: help, exit, jdi <ID>, utok <ID>, batoh, prohledat, nakup, mluvit");
        }

        System.out.println("Stiskněte 'Enter' pro pokračování..\n");
        System.out.println(oddelovac);
        sc.nextLine();
        clearConsole();
    }

    /**
     * Prompts the user to enter their name and sets it as the player's name.
     */
    private static void setUpGamerName() {
        boolean run = true;
        clearConsole();
        while (run) {
            System.out.println(oddelovac + "\n");
            System.out.print("Zadejte své jméno: ");
            String input = sc.nextLine();
            if (input.length() > 0 && input.length() < 15) {
                GH.game.getPlayer().setName(input);
                run = false;
            } else {
                System.out.println("Jméno musí mít alespoň jeden znak a méně než 15 znaků.\n");
                wait(2000);
            }
            clearConsole();
        }

    }

    /**
     * Prompts the user to select a difficulty level and updates the game accordingly.
     */
    private static void setUpDificulty() {
        boolean run = true;
        Integer input = 0;
        clearConsole();
        while (run) {
            System.out.println(oddelovac + "\n");
            System.out.println("Obtiznosti hry:\n 1. Jednoducha\n 2. Normalni\n 3. Tezka\n");
            System.out.print("Vyberte: ");
            try {
                input = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Nezadali jste celé číslo!");
            }
            if (input != null && input > 0 && input < 4) {
                run = false;
            } else {
                System.out.println("Zadejte prosím celé číslo v rozmezí od 1 do 3.");
                wait(2000);
            }
            clearConsole();
        }
        Difficulty difficulty = Difficulty.EASY;
        switch (input) {
            case 1:
                difficulty = Difficulty.EASY;
                break;
            case 2:
                difficulty = Difficulty.MEDIUM;
                break;
            case 3:
                difficulty = Difficulty.HARD;
                break;
            default:
                System.out.println("Něco je hodně špatně!");
                System.out.println("Stiskněte 'Enter' pro ukončení..\n");
                System.exit(0);
                break;
        }
        GH.setGameDifficulty(difficulty);
    }

    /**
     * Loads and initializes the game map using GameHandler and displays confirmation.
     */
    private static void setUpMap() {
        try {
            GH.ImportMap();
            System.out.println("Mapa " + GH.game.getMap().getSeed() + " byla úspěšně načtena.");
            wait(1000);
            clearConsole();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Stiskněte 'Enter' pro ukončení..\n");
            System.exit(0);
        }
    }

    /**
     * Runs the main game loop, processing user commands until the game ends
     * due to player death or victory.
     */
    private static void play() {
        boolean run = true;
        clearConsole();
        System.out.println(oddelovac + "\n");

        try {
            TxtHandler th = new TxtHandler();
            String tutorial = th.loadTXT(INTRO_FILE);
            System.out.println(tutorial);
        } catch (Exception e) {
            System.out.println("Nepodařilo se načíst úvod.");
            System.out.println("Jednoho krásného dne se probudíš v pokoji v hospodě. Králův osobný strážný tě žádá abys ses zastavil u krále.");
        }

        while (run) {
            System.out.println(GH.game.getPlayer().fullToString());
            System.out.println("Nachazis se v " + GH.game.getCurrentRoom().getName() + "\n");
            System.out.print("\nZadejte prikaz: ");
            String userInput = sc.nextLine().toLowerCase();

            Command command = null;

            try {
                command = CP.parseCommand(userInput);
                if (command != null) {
                    command.execute();
                }
                if (GH.game.getCurrentRoom().getId() == 15) {
                    if (GH.game.searchInInventory("Pecet") != null) {
                        throw new WinException("Došel jsi v pořádku s pečetí do druhého hradu.");
                    } else {
                        System.out.println("Nedonesl jsi Královskou pečeť. Proto tě nemůžeme pustit do hradu. Běž zpátky a přines ji.");
                        System.out.println(oddelovac + "\n");
                    }
                }
                System.out.println(oddelovac + "\n");
            } catch (DeathException e) {
                System.out.println(e.getMessage());
                run = false;
                konec(false);
            } catch (WinException e) {
                System.out.println(e.getMessage());
                run = false;
                konec(true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                wait(2000);
                System.out.println(oddelovac + "\n");
            }

            // Provizorní kontrola smrti (kdyby nefungoval DeathException)
            if (!GH.game.getPlayer().isAlive()) {
                run = false;
                konec(false);
            }
            clearConsole();
        }
    }

    /**
     * Displays the end-of-game screen based on whether the player has won or lost,
     * and shows tutorial text if the player lost.
     *
     * @param win true if the player won the game, false if the player died
     */
    public static void konec(Boolean win) {
        saveCurrentResultToCsv();
        if (win) {
            System.out.println("Gratuluji " + GH.game.getPlayer().getName() + " k výhře! Zachránil jste království. \n");
        } else {
            System.out.println("Bohužel se ti nepovedlo zachránit království. \n");
        }
        System.out.println("Chcete hrát znovu? (ano/ne)");
        String input = sc.nextLine().toLowerCase();
        if (input.startsWith("a")) {
            restart();
        } else {
            System.out.println("Ukončuji hru. Nashledanou!");
            System.exit(0);
        }
    }

    private static void saveCurrentResultToCsv() {
        try {
            BEST_RESULTS_SERVICE.recordResult(
                    GH.game.getPlayer().getName(),
                    GH.game.getPlayer().getMoney(),
                    GH.game.getDifficulty()
            );
        } catch (RuntimeException e) {
            System.out.println("Nepodarilo se ulozit vysledek do CSV: " + e.getMessage());
        }
    }

    /**
     * Clears the console by printing the appropriate ASCII escape sequence.
     */
    public static void clearConsole() {
        // ASCII escape sekvence
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Pauses execution for the specified amount of time in milliseconds.
     *
     * @param time delay duration in milliseconds
     */
    public static void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
