package cz.vse.semestralka_4it115.main;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.space.Difficulty;
import cz.vse.semestralka_4it115.logic.space.Room;
import cz.vse.semestralka_4it115.serializer.TxtHandler;
import cz.vse.semestralka_4it115.ui.game.GameHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * JavaFX controller for the main game window.
 * Initializes a playable game session and routes command input from GUI to game logic.
 */
public class MainController {
    private static final String HELP_FILE = "resources\\help.txt";
    private final GuiCommandExecutor commandExecutor = new GuiCommandExecutor();
    private final Set<Integer> visitedRoomIds = new HashSet<>();
    private final ObservableList<LogEntry> logEntries = FXCollections.observableArrayList();

    @FXML
    private Label playerName;
    @FXML
    private Label playerHealth;
    @FXML
    private Label playerStrength;
    @FXML
    private Label playerDefence;
    @FXML
    private Label playerMoney;
    @FXML
    private TextField cmdInput;
    @FXML
    private TextArea playerBackpack;
    @FXML
    private ListView<LogEntry> systemLog;
    @FXML
    private ImageView map;
    @FXML
    private TextArea questList;
    @FXML
    private VBox exitsBox;

    /**
     * Initializes GUI state and starts a default game session for graphical mode.
     */
    @FXML
    public void initialize() {
        setUpSystemLog();
        startNewGame();
    }

    /**
     * Starts a new game for GUI mode and writes initial information to system log.
     */
    private void startNewGame() {
        GameHandler.game = new cz.vse.semestralka_4it115.ui.game.Game();
        String playerNameInput = requestPlayerName();
        Difficulty difficultyInput = requestDifficulty();
        GameHandler.game.getPlayer().setName(playerNameInput);
        GameHandler.setGameDifficulty(difficultyInput);

        try {
            GameHandler.ImportMap();
        } catch (Exception e) {
            appendErrorLog("Nepodařilo se načíst mapu: " + e.getMessage());
            return;
        }

        visitedRoomIds.clear();
        appendIntroDialogue();
        appendGameLog("Napiš \"help\" pro nápovědu.");
        appendSystemLog("Nacházíš se v " + GameHandler.game.getCurrentRoom().getName() + ".");
        appendSystemLog(GameHandler.game.getCurrentRoom().getDescription());
        refreshView();
    }

    /**
     * Writes the opening story and short guard conversation to the system log.
     */
    private void appendIntroDialogue() {
        String playerName = GameHandler.game.getPlayer().getName();
        appendGameLog("Jednoho krásného dne se probudíš v pokoji v hospodě.");
        appendDialogLog("Strážný: \"Dobré ráno, " + playerName + ". Král tě žádá, abys ses okamžitě zastavil u něj v trůnním sále.\"");
        appendDialogLog(playerName + ": \"Rozumím. Hned se za králem vydám.\"");
    }

    /**
     * Gets the cmd input.
     * @return
     */
    public String getCmdInput() {
        return cmdInput.getText();
    }

    /**
     * Sets the player's name.
     * @param playerName
     */
    public void setPlayerName(String playerName) {
        this.playerName.setText(playerName);
    }

    /**
     * Sets the player's health.
     * @param playerHealth
     */
    public void setPlayerHealth(int playerHealth) {
        this.playerHealth.setText(String.valueOf(playerHealth));
    }

    /**
     * Sets the player's strength.
     * @param playerStrength
     */
    public void setPlayerStrength(int playerStrength) {
        this.playerStrength.setText(String.valueOf(playerStrength));
    }

    /**
     * Sets the player's defence.
     * @param playerDefence
     */
    public void setPlayerDefence(int playerDefence) {
        this.playerDefence.setText(String.valueOf(playerDefence));
    }
    /**
     * Sets the player's money.
     * @param playerMoney
     */
    public void setPlayerMoney(int playerMoney) {
        this.playerMoney.setText(String.valueOf(playerMoney));
    }

    /**
     * Sets the cmd input.
     * @param input
     */
    public void setCmdInput(String input) {
        this.cmdInput.setText(input);
    }

    /**
     * Sets the quest list.
     * @param input
     */
    public void setQuestList(String input) {
        this.questList.setText(input);
    }

    /**
     * Sets the system log.
     * @param input
     */
    public void setSystemLog(String input) {
        logEntries.clear();
        if (input != null && !input.isBlank()) {
            appendSystemLog(input);
        }
    }

    /**
     * Sets the player's backpack.
     * @param input
     */
    public void setPlayerBackpack(String input) {
        this.playerBackpack.setText(input);
    }
    /**
     * Sets the map.
     * @param input
     */
    public void setMap(String input) {
        this.map.setImage(new javafx.scene.image.Image(input));
    }

    /**
     *
     * @param actionEvent
     */
    public void sendCmdInput(ActionEvent actionEvent) {
        String input = getCmdInput();
        if (input == null || input.isBlank()) {
            clearCmdInput();
            return;
        }

        input = input.trim();
        appendPlayerLog(input);

        if (input.equalsIgnoreCase("help")) {
            showHelpInLog();
        } else {
            executeCommandAndLogOutput(input.toLowerCase());
        }

        refreshView();
        clearCmdInput();
    }

    /**
     * Starts a brand new game from GUI action.
     *
     * @param actionEvent menu/button action
     */
    public void newGame(ActionEvent actionEvent) {
        setSystemLog("");
        startNewGame();
    }

    /**
     * Restarts the current game (same behavior as New Game in GUI).
     *
     * @param actionEvent menu/button action
     */
    public void restartGame(ActionEvent actionEvent) {
        setSystemLog("");
        startNewGame();
    }

    /**
     * Closes the application.
     *
     * @param actionEvent menu/button action
     */
    public void closeApp(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Executes parsed command and captures command output so it is visible in GUI log.
     *
     * @param input user command
     */
    private void executeCommandAndLogOutput(String input) {
        int previousRoomId = getCurrentRoomId();
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
        boolean commandSuccess = false;

        try (PrintStream redirectedOut = new PrintStream(outputBuffer, true, StandardCharsets.UTF_8)) {
            System.setOut(redirectedOut);
            commandExecutor.execute(input);
            commandSuccess = true;
        } catch (Exception e) {
            appendErrorLog(e.getMessage());
        } finally {
            System.setOut(originalOut);
        }

        String commandOutput = outputBuffer.toString(StandardCharsets.UTF_8).trim();
        if (!commandOutput.isEmpty()) {
            appendGameLog(commandOutput);
        }

        if (commandSuccess) {
            appendLocationAfterMove(previousRoomId);
        }
    }

    /**
     * Loads help text and writes it to system log without waiting for console input.
     */
    private void showHelpInLog() {
        String helpText = new TxtHandler().loadTXT(HELP_FILE).trim();
        appendGameLog(helpText);
    }

    /**
     * Refreshes player labels and side panels based on current game state.
     */
    private void refreshView() {
        Person player = GameHandler.game.getPlayer();
        setPlayerName(player.getName());
        setPlayerHealth((int) Math.round(player.getHealth()));
        setPlayerStrength((int) Math.round(player.getEffectiveStrength()));
        setPlayerDefence((int) Math.round(player.getEffectiveDefense()));
        setPlayerMoney(player.getMoney());

        if (player.getInventory().getItems().isEmpty()) {
            setPlayerBackpack("Batoh je prázdný.");
        } else {
            setPlayerBackpack(player.getInventory().toString());
        }

        if (GameHandler.game.getCurrentRoom() != null) {
            visitedRoomIds.add(GameHandler.game.getCurrentRoom().getId());
        }
        updateExits();
        updateQuestList();
    }

    /**
     * Appends one message line to GUI system log.
     *
     * @param message message to append
     */
    private void appendSystemLog(String message) {
        appendGameLog(message);
    }

    /**
     * Adds player command to visual log.
     *
     * @param message player command
     */
    private void appendPlayerLog(String message) {
        appendLog(message, LogType.PLAYER);
    }

    /**
     * Adds game output to visual log.
     *
     * @param message game output
     */
    private void appendGameLog(String message) {
        appendLog(message, LogType.GAME);
    }

    /**
     * Adds error output to visual log.
     *
     * @param message error output
     */
    private void appendErrorLog(String message) {
        appendLog(message, LogType.ERROR);
    }

    /**
     * Adds dialogue line to visual log.
     *
     * @param message dialogue output
     */
    private void appendDialogLog(String message) {
        appendLog(message, LogType.DIALOG);
    }

    /**
     * Adds one or more lines into log list with style info.
     *
     * @param message text to append
     * @param logType message kind
     */
    private void appendLog(String message, LogType logType) {
        if (message == null || message.isBlank()) {
            return;
        }

        String[] lines = message.split("\\R");
        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }
            logEntries.add(new LogEntry(line, logType));
        }
        if (!logEntries.isEmpty()) {
            systemLog.scrollTo(logEntries.size() - 1);
        }
    }

    /**
     * Configures list cell rendering for player/game log messages.
     */
    private void setUpSystemLog() {
        systemLog.setItems(logEntries);
        systemLog.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(LogEntry item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                if (item.type() == LogType.PLAYER) {
                    setText("> " + item.message());
                    setStyle("-fx-text-fill: #0D47A1; -fx-font-weight: bold; -fx-font-size: 15px;");
                } else if (item.type() == LogType.ERROR) {
                    setText(item.message());
                    setStyle("-fx-text-fill: #B71C1C; -fx-font-weight: bold; -fx-font-size: 15px;");
                } else if (item.type() == LogType.DIALOG) {
                    setText(item.message());
                    setStyle("-fx-text-fill: #6D4C41; -fx-font-style: italic; -fx-font-size: 15px;");
                } else {
                    setText(item.message());
                    setStyle("-fx-text-fill: #1B5E20; -fx-font-size: 15px;");
                }
            }
        });
    }

    /**
     * Rebuilds clickable exits based on current room.
     */
    private void updateExits() {
        exitsBox.getChildren().clear();

        if (GameHandler.game.getCurrentRoom() == null) {
            return;
        }

        java.util.List<Room> exits = GameHandler.game.getMap().getCurrentExits();
        if (exits.isEmpty()) {
            Button noExitButton = new Button("Žádné dostupné východy");
            noExitButton.setDisable(true);
            noExitButton.setMaxWidth(Double.MAX_VALUE);
            noExitButton.setStyle("-fx-font-size: 14px;");
            exitsBox.getChildren().add(noExitButton);
            return;
        }

        for (int i = 0; i < exits.size(); i++) {
            int exitIndex = i + 1;
            Room room = exits.get(i);

            String buttonLabel = room.getName();
            if (room.isLocked()) {
                buttonLabel += " (zamčená)";
            }

            Button exitButton = new Button(buttonLabel);
            exitButton.setMaxWidth(Double.MAX_VALUE);
            exitButton.setStyle("-fx-font-size: 14px;");
            exitButton.setOnAction(event -> moveToExit(exitIndex, room.getName()));
            exitsBox.getChildren().add(exitButton);
        }
    }

    /**
     * Moves player to selected exit by click.
     *
     * @param exitIndex index in current exits list
     * @param roomName room label for the log
     */
    private void moveToExit(int exitIndex, String roomName) {
        appendPlayerLog("jdi " + roomName + " (klik)");
        executeCommandAndLogOutput("jdi " + exitIndex);
        refreshView();
    }

    /**
     * Asks user for player name; enforces the same limits as text version.
     *
     * @return validated player name
     */
    private String requestPlayerName() {
        while (true) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nová hra");
            dialog.setHeaderText("Zadej jméno hráče");
            dialog.setContentText("Jméno (1-14 znaků):");

            Optional<String> result = dialog.showAndWait();
            if (result.isEmpty()) {
                return "Hráč";
            }

            String candidate = result.get().trim();
            if (!candidate.isEmpty() && candidate.length() < 15) {
                return candidate;
            }
        }
    }

    /**
     * Asks user for game difficulty in a GUI dialog.
     *
     * @return selected difficulty (default EASY if canceled)
     */
    private Difficulty requestDifficulty() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Jednoduchá", "Jednoduchá", "Normální", "Těžká");
        dialog.setTitle("Nová hra");
        dialog.setHeaderText("Vyber obtížnost hry");
        dialog.setContentText("Obtiznost:");

        Optional<String> result = dialog.showAndWait();
        String selected = result.orElse("Jednoduchá");

        return switch (selected) {
            case "Normální" -> Difficulty.MEDIUM;
            case "Těžká" -> Difficulty.HARD;
            default -> Difficulty.EASY;
        };
    }

    /**
     * Updates quest panel so it shows static objectives and removes completed ones.
     */
    private void updateQuestList() {
        Map<String, Boolean> objectives = new LinkedHashMap<>();
        objectives.put("- Jdi za Králem do trůnního sálu a seber \"Pečeť\"", isTaskOneDone());
        objectives.put("- Najdi ve hradě \"Klíč\" k tajné chodbě a odejdi z hradu tajnou chodbou", isTaskTwoDone());
        objectives.put("- Zabij nepřítele, které potkáš po cestě a doruč pečeť do druhého hradu", isTaskThreeDone());

        StringBuilder sb = new StringBuilder();
        sb.append("Tvé úkoly:\n");
        boolean hasAnyOpenTask = false;

        for (Map.Entry<String, Boolean> objective : objectives.entrySet()) {
            if (!objective.getValue()) {
                sb.append(objective.getKey()).append("\n");
                hasAnyOpenTask = true;
            }
        }

        if (!hasAnyOpenTask) {
            sb.append("- Všechny úkoly jsou splněny.");
        }

        setQuestList(sb.toString().trim());
    }

    /**
     * First objective: player has the seal in inventory.
     */
    private boolean isTaskOneDone() {
        return hasInventoryItem("pečeť");
    }

    /**
     * Second objective: player has key and already left the secret passage area to outside rooms.
     */
    private boolean isTaskTwoDone() {
        if (!hasInventoryItem("klíč")) {
            return false;
        }
        return GameHandler.game.getCurrentRoom() != null && GameHandler.game.getCurrentRoom().getId() >= 11;
    }

    /**
     * Third objective: player reached final room with seal and all enemies in visited rooms are dead.
     */
    private boolean isTaskThreeDone() {
        if (!hasInventoryItem("pečeť")) {
            return false;
        }
        if (GameHandler.game.getCurrentRoom() == null || GameHandler.game.getCurrentRoom().getId() != 15) {
            return false;
        }
        for (Room room : GameHandler.game.getMap().getLayout()) {
            if (!visitedRoomIds.contains(room.getId())) {
                continue;
            }
            if (room.getOtherPeople() == null) {
                continue;
            }
            boolean hasLivingEnemy = room.getOtherPeople().stream()
                    .anyMatch(p -> !p.isPeaceful() && p.isAlive());
            if (hasLivingEnemy) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks inventory by normalized name prefix (diacritics-insensitive).
     *
     * @param expectedPrefix item prefix to find
     * @return true if item exists in inventory
     */
    private boolean hasInventoryItem(String expectedPrefix) {
        String normalizedPrefix = normalize(expectedPrefix);
        return GameHandler.game.getPlayer().getInventory().getItems().stream()
                .map(item -> normalize(item.getName()))
                .anyMatch(name -> name.startsWith(normalizedPrefix));
    }

    /**
     * Normalizes Czech text to ascii-like lowercase text.
     */
    private String normalize(String value) {
        String noDiacritics = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return noDiacritics.toLowerCase();
    }

    /**
     * Returns current room id or -1 when unavailable.
     */
    private int getCurrentRoomId() {
        if (GameHandler.game == null || GameHandler.game.getCurrentRoom() == null) {
            return -1;
        }
        return GameHandler.game.getCurrentRoom().getId();
    }

    /**
     * Writes current location details to system log when player changes room.
     *
     * @param previousRoomId room id before command execution
     */
    private void appendLocationAfterMove(int previousRoomId) {
        if (GameHandler.game == null || GameHandler.game.getCurrentRoom() == null) {
            return;
        }
        Room currentRoom = GameHandler.game.getCurrentRoom();
        if (currentRoom.getId() == previousRoomId) {
            return;
        }
        appendGameLog("Nacházíš se v " + currentRoom.getName() + ".");
        appendGameLog(currentRoom.getDescription());
    }

    /**
     * Clears the cmd input.
     */
    public void clearCmdInput(){
        setCmdInput("");
    }

    /**
     * Category of one log line used for styling.
     */
    private enum LogType {
        PLAYER,
        GAME,
        ERROR,
        DIALOG
    }

    /**
     * Data holder for system log line.
     *
     * @param message log text
     * @param type kind of message
     */
    private record LogEntry(String message, LogType type) {
    }
}
