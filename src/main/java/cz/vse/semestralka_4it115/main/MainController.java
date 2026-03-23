package cz.vse.semestralka_4it115.main;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.item.Item;
import cz.vse.semestralka_4it115.logic.space.Difficulty;
import cz.vse.semestralka_4it115.logic.space.Room;
import cz.vse.semestralka_4it115.score.BestResultsService;
import cz.vse.semestralka_4it115.serializer.TxtHandler;
import cz.vse.semestralka_4it115.ui.game.FightUI;
import cz.vse.semestralka_4it115.ui.game.GameHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * JavaFX controller for the main game window.
 * Initializes a playable game session and routes command input from GUI to game logic.
 */
public class MainController {
    private static final String HELP_FILE = "resources\\help.txt";
    private static final String MAP_IMAGE_BASE_PATH = "/cz/vse/semestralka_4it115/main/img/maps/";
    private static final String DEFAULT_MAP_IMAGE = "map.png";
    private static final String ITEM_IMAGE_BASE_PATH = "/cz/vse/semestralka_4it115/main/img/items/";
    private static final String DEFAULT_ITEM_IMAGE = "Default.png";
    private final GuiCommandExecutor commandExecutor = new GuiCommandExecutor(this::showLargeMapWindow);
    private final BestResultsService bestResultsService = BestResultsService.createDefault();
    private Trade trade;
    private final Set<Integer> visitedRoomIds = new HashSet<>();
    private final ObservableList<LogEntry> logEntries = FXCollections.observableArrayList();
    private boolean gameOver = false;
    private boolean commandInProgress = false;
    private boolean endGamePopupShown = false;

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
    private ListView<ItemListEntry> playerBackpack;
    @FXML
    private ListView<ItemListEntry> roomItems;
    @FXML
    private Label backpackWeight;
    @FXML
    private ListView<LogEntry> systemLog;
    @FXML
    private ImageView map;
    @FXML
    private TextArea questList;
    @FXML
    private VBox peopleBox;
    @FXML
    private VBox exitsBox;

    /**
     * Initializes GUI state and starts a default game session for graphical mode.
     */
    @FXML
    public void initialize() {
        trade = new Trade(
                () -> cmdInput != null && cmdInput.getScene() != null ? cmdInput.getScene().getWindow() : null,
                this::refreshView,
                this::appendGameLog,
                this::appendErrorLog
        );
        setUpSystemLog();
        setUpItemLists();
        startNewGame();
    }

    /**
     * Starts a new game for GUI mode and writes initial information to system log.
     */
    private void startNewGame() {
        startGameSession(null);
    }

    /**
     * Restarts the game while preserving current player name and asking only for difficulty.
     */
    private void restartWithCurrentPlayerName() {
        String currentPlayerName = null;
        if (GameHandler.game != null && GameHandler.game.getPlayer() != null) {
            currentPlayerName = GameHandler.game.getPlayer().getName();
        }
        startGameSession(currentPlayerName);
    }

    /**
     * Initializes a fresh game session and asks only for inputs that are not preset.
     *
     * @param presetPlayerName player name to reuse; if null/blank, user is prompted for a new one
     */
    private void startGameSession(String presetPlayerName) {
        GameHandler.game = new cz.vse.semestralka_4it115.ui.game.Game();
        gameOver = false;
        commandInProgress = false;
        endGamePopupShown = false;
        String playerNameInput = presetPlayerName;
        if (playerNameInput == null || playerNameInput.isBlank()) {
            playerNameInput = requestPlayerName();
            if (playerNameInput == null) {
                Platform.exit();
                return;
            }
        }
        Difficulty difficultyInput = requestDifficulty();
        if (difficultyInput == null) {
            Platform.exit();
            return;
        }
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
        if (input == null || input.isBlank()) {
            this.playerBackpack.getItems().clear();
            return;
        }
        ObservableList<ItemListEntry> entries = FXCollections.observableArrayList();
        for (String line : input.split("\\R")) {
            if (!line.isBlank()) {
                entries.add(new ItemListEntry(line.trim(), 0, true));
            }
        }
        this.playerBackpack.setItems(entries);
    }
    /**
     * Sets the map.
     * @param input
     */
    public void setMap(String input) {
        this.map.setImage(new Image(input));
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
     * Restarts current game state while keeping player name and reselecting difficulty.
     *
     * @param actionEvent menu/button action
     */
    public void restartGame(ActionEvent actionEvent) {
        setSystemLog("");
        restartWithCurrentPlayerName();
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
     * Changes current player name without restarting the game.
     *
     * @param actionEvent menu action
     */
    public void changePlayerName(ActionEvent actionEvent) {
        if (GameHandler.game == null || GameHandler.game.getPlayer() == null) {
            appendErrorLog("Hra neni pripravena na zmenu jmena.");
            return;
        }

        String currentName = GameHandler.game.getPlayer().getName();
        String newName = PlayerNameDialog.requestName(
                cmdInput == null || cmdInput.getScene() == null ? null : cmdInput.getScene().getWindow(),
                "Zmena jmena hrace",
                currentName
        );

        if (newName == null || newName.equals(currentName)) {
            return;
        }

        GameHandler.game.getPlayer().setName(newName);
        setPlayerName(newName);
        appendGameLog("Jmeno hrace bylo zmeneno na: " + newName + ".");
    }

    /**
     * Opens local HTML help file in a modal window.
     *
     * @param actionEvent menu action
     */
    public void openHelpHtml(ActionEvent actionEvent) {
        try {
            HelpHtmlDialog.show(cmdInput == null || cmdInput.getScene() == null ? null : cmdInput.getScene().getWindow());
        } catch (RuntimeException e) {
            appendErrorLog("Nepodarilo se otevrit HTML napovedu: " + e.getMessage());
        }
    }

    /**
     * Opens enlarged map window from GUI button.
     *
     * @param actionEvent button click event
     */
    public void openLargeMapWindow(ActionEvent actionEvent) {
        showLargeMapWindow();
    }

    /**
     * Opens table with stored best results.
     *
     * @param actionEvent menu/button action
     */
    public void openBestResults(ActionEvent actionEvent) {
        showBestResultsWindow();
    }

    /**
     * Drops selected backpack item to current room.
     *
     * @param actionEvent button click event
     */
    public void dropSelectedBackpackItem(ActionEvent actionEvent) {
        ItemListEntry selectedItem = playerBackpack.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.placeholder()) {
            appendErrorLog("Vyberte věc z batohu, kterou chcete vyhodit.");
            return;
        }

        String itemName = selectedItem.name();
        appendPlayerLog("batoh zahod " + itemName + " (klik)");
        executeCommandAndLogOutput("batoh zahod " + itemName.toLowerCase());
        refreshView();
    }

    /**
     * Uses selected backpack item.
     *
     * @param actionEvent button click event
     */
    public void useSelectedBackpackItem(ActionEvent actionEvent) {
        ItemListEntry selectedItem = playerBackpack.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.placeholder()) {
            appendErrorLog("Vyberte věc z batohu, kterou chcete použít.");
            return;
        }

        String itemName = selectedItem.name();
        appendPlayerLog("batoh pouzij " + itemName + " (klik)");
        executeCommandAndLogOutput("batoh pouzij " + itemName.toLowerCase());
        refreshView();
    }

    /**
     * Takes selected room item to player's backpack.
     *
     * @param actionEvent button click event
     */
    public void takeSelectedRoomItem(ActionEvent actionEvent) {
        ItemListEntry selectedItem = roomItems.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.placeholder()) {
            appendErrorLog("Vyberte věc v místnosti, kterou chcete vzít.");
            return;
        }

        String itemName = selectedItem.name();
        appendPlayerLog("batoh vezmi " + itemName + " (klik)");
        executeCommandAndLogOutput("batoh vezmi " + itemName.toLowerCase());
        refreshView();
    }

    private void executeCommandAndLogOutput(String input) {
        if (gameOver) {
            appendErrorLog("Hra již skončila. Zvol New game nebo Restart.");
            return;
        }
        if (commandInProgress) {
            appendErrorLog("Probíhá akce, počkejte na dokončení.");
            return;
        }
        if (trade.tryHandleTradeCommand(input)) {
            handleEndGameState();
            return;
        }
        if (input != null && input.toLowerCase().startsWith("utok ")) {
            executeAttackCommandAsync(input);
            return;
        }

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
            if (commandSuccess && isTalkCommand(input)) {
                appendDialogLog(commandOutput);
            } else {
                appendGameLog(commandOutput);
            }
        }

        if (commandSuccess) {
            appendLocationAfterMove(previousRoomId);
        }

        handleEndGameState();
    }

    private boolean isTalkCommand(String input) {
        if (input == null || input.isBlank()) {
            return false;
        }
        String normalizedInput = input.trim().toLowerCase();
        return normalizedInput.equals("mluvit") || normalizedInput.startsWith("mluvit ");
    }

    private void executeAttackCommandAsync(String input) {
        String[] tokens = input.trim().toLowerCase().split("\\s+");
        if (tokens.length < 2) {
            appendErrorLog("Pro příkaz 'utok' je potřeba zadat osobu.");
            return;
        }

        commandInProgress = true;
        int previousRoomId = getCurrentRoomId();
        String target = tokens[1];

        Thread combatThread = new Thread(() -> {
            try {
                FightUI.start(target, new FightUI.FightLogSink() {
                    @Override
                    public void player(String message) {
                        Platform.runLater(() -> appendCombatPlayerLog(message));
                    }

                    @Override
                    public void enemy(String message) {
                        Platform.runLater(() -> appendCombatEnemyLog(message));
                    }

                    @Override
                    public void system(String message) {
                        Platform.runLater(() -> appendGameLog(message));
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> appendErrorLog(e.getMessage()));
            } finally {
                Platform.runLater(() -> {
                    commandInProgress = false;
                    appendLocationAfterMove(previousRoomId);
                    handleEndGameState();
                    refreshView();
                });
            }
        }, "fight-thread");
        combatThread.setDaemon(true);
        combatThread.start();
    }

    /**
     * Detects end-game states after each successful command in GUI mode.
     */
    private void handleEndGameState() {
        if (GameHandler.game == null || GameHandler.game.getPlayer() == null) {
            return;
        }

        if (!GameHandler.game.getPlayer().isAlive()) {
            gameOver = true;
            saveCurrentResultToCsv();
            showEndGamePopup(false);
            appendErrorLog("Prohrál jsi. Hra skončila. Zvol New game nebo Restart.");
            return;
        }

        if (GameHandler.game.getCurrentRoom() != null
                && GameHandler.game.getCurrentRoom().getId() == 15
                && GameHandler.game.searchInInventory("Pecet") != null) {
            gameOver = true;
            saveCurrentResultToCsv();
            showEndGamePopup(true);
            appendGameLog("Vyhrál jsi! Hra skončila. Zvol New game nebo Restart.");
        }
    }

    private void saveCurrentResultToCsv() {
        Person player = GameHandler.game.getPlayer();
        try {
            bestResultsService.recordResult(player.getName(), player.getMoney(), GameHandler.game.getDifficulty());
        } catch (RuntimeException e) {
            appendErrorLog("Nepodarilo se ulozit vysledek do CSV: " + e.getMessage());
        }
    }

    private void showEndGamePopup(boolean win) {
        if (endGamePopupShown) {
            return;
        }
        endGamePopupShown = true;
        EndGameDialog.show(
                cmdInput == null || cmdInput.getScene() == null ? null : cmdInput.getScene().getWindow(),
                win,
                () -> {
                    setSystemLog("");
                    restartWithCurrentPlayerName();
                },
                Platform::exit
        );
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
        updateMapImage();

        if (player.getInventory().getItems().isEmpty()) {
            setPlayerBackpack("Batoh je prázdný.");
        } else {
            setPlayerBackpack(player.getInventory().toString());
        }
        updatePlayerBackpackItems(player);

        if (GameHandler.game.getCurrentRoom() != null) {
            visitedRoomIds.add(GameHandler.game.getCurrentRoom().getId());
        }
        updatePeopleInRoom();
        updateExits();
        updateRoomItems();
        updateQuestList();
    }

    /**
     * Refreshes backpack entries and carried weight indicator.
     *
     * @param player current player
     */
    private void updatePlayerBackpackItems(Person player) {
        ObservableList<ItemListEntry> backpackEntries = FXCollections.observableArrayList();
        for (Item item : player.getInventory().getItems()) {
            backpackEntries.add(new ItemListEntry(item.getName(), item.getWeight(), false));
        }

        if (backpackEntries.isEmpty()) {
            backpackEntries.add(new ItemListEntry("Batoh je prázdný.", 0, true));
        }
        playerBackpack.setItems(backpackEntries);

        backpackWeight.setText(String.format("Neseno: %.1f / %.1f kg",
                player.getInventory().getCurrentWeight(),
                player.getInventory().getMaxWeight()));
    }

    /**
     * Refreshes list of items available in current room.
     */
    private void updateRoomItems() {
        ObservableList<ItemListEntry> roomItemEntries = FXCollections.observableArrayList();
        if (GameHandler.game != null && GameHandler.game.getCurrentRoom() != null) {
            for (Item item : GameHandler.game.getCurrentRoom().getItems()) {
                roomItemEntries.add(new ItemListEntry(item.getName(), item.getWeight(), false));
            }
        }

        if (roomItemEntries.isEmpty()) {
            roomItemEntries.add(new ItemListEntry("V místnosti nejsou žádné věci.", 0, true));
        }
        roomItems.setItems(roomItemEntries);
    }

    /**
     * Configures list rendering with item icons and click interactions.
     */
    private void setUpItemLists() {
        playerBackpack.setCellFactory(list -> new ItemListCell(this::loadItemIcon));
        roomItems.setCellFactory(list -> new ItemListCell(this::loadItemIcon));
    }

    /**
     * Loads item icon by item name with fallback to Default.png.
     *
     * @param itemName game item name
     * @return image for list cell
     */
    private Image loadItemIcon(String itemName) {
        URL iconUrl = null;
        if (itemName != null && !itemName.isBlank()) {
            String fileName = itemName + ".png";
            iconUrl = getClass().getResource(ITEM_IMAGE_BASE_PATH + fileName);
        }
        if (iconUrl == null) {
            iconUrl = getClass().getResource(ITEM_IMAGE_BASE_PATH + DEFAULT_ITEM_IMAGE);
        }
        if (iconUrl == null) {
            return null;
        }
        return new Image(iconUrl.toExternalForm());
    }

    /**
     * Updates map image according to current room id.
     * Tries map_<roomId>.png first, then falls back to map.png.
     */
    private void updateMapImage() {
        URL mapUrl = resolveCurrentMapUrl();
        if (mapUrl != null) {
            map.setImage(new Image(mapUrl.toExternalForm()));
        }
    }

    /**
     * Resolves current room map resource URL with fallback to default map.
     *
     * @return URL to map image or null if not found
     */
    private URL resolveCurrentMapUrl() {
        if (GameHandler.game == null || GameHandler.game.getCurrentRoom() == null) {
            return getClass().getResource(MAP_IMAGE_BASE_PATH + DEFAULT_MAP_IMAGE);
        }

        int roomId = GameHandler.game.getCurrentRoom().getId();
        String roomMapFile = "map_" + roomId + ".png";
        URL roomMapUrl = getClass().getResource(MAP_IMAGE_BASE_PATH + roomMapFile);
        if (roomMapUrl != null) {
            return roomMapUrl;
        }
        return getClass().getResource(MAP_IMAGE_BASE_PATH + DEFAULT_MAP_IMAGE);
    }

    /**
     * Opens separate window with enlarged current map image.
     */
    private void showLargeMapWindow() {
        URL mapUrl = resolveCurrentMapUrl();
        if (mapUrl == null) {
            appendErrorLog("Mapu se nepodařilo načíst.");
            return;
        }

        ImageView largeMapView = new ImageView(new Image(mapUrl.toExternalForm()));
        largeMapView.setPreserveRatio(true);
        largeMapView.setFitWidth(1000);
        largeMapView.setFitHeight(700);

        StackPane root = new StackPane(largeMapView);
        root.setStyle("-fx-padding: 12px; -fx-background-color: #f0f0f0;");

        Stage stage = new Stage();
        stage.setTitle("Mapa - zvětšené zobrazení");
        if (map.getScene() != null && map.getScene().getWindow() != null) {
            stage.initOwner(map.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
        } else {
            stage.initModality(Modality.APPLICATION_MODAL);
        }
        stage.setScene(new Scene(root, 1050, 760));
        stage.showAndWait();
    }

    private void showBestResultsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("best-results.fxml"));
            Parent root = loader.load();
            BestResultsController controller = loader.getController();
            controller.setResults(bestResultsService.getBestResults());

            Stage stage = new Stage();
            stage.setTitle("Nejlepsi vysledky");
            if (cmdInput != null && cmdInput.getScene() != null && cmdInput.getScene().getWindow() != null) {
                stage.initOwner(cmdInput.getScene().getWindow());
                stage.initModality(Modality.WINDOW_MODAL);
            } else {
                stage.initModality(Modality.APPLICATION_MODAL);
            }
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            appendErrorLog("Nepodarilo se otevrit vysledky: " + e.getMessage());
        }
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

    private void appendCombatPlayerLog(String message) {
        appendLog(message, LogType.COMBAT_PLAYER);
    }

    private void appendCombatEnemyLog(String message) {
        appendLog(message, LogType.COMBAT_ENEMY);
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
                } else if (item.type() == LogType.COMBAT_PLAYER) {
                    setText(item.message());
                    setStyle("-fx-text-fill: #1565C0; -fx-font-weight: bold; -fx-font-size: 15px;");
                } else if (item.type() == LogType.COMBAT_ENEMY) {
                    setText(item.message());
                    setStyle("-fx-text-fill: #C62828; -fx-font-weight: bold; -fx-font-size: 15px;");
                } else {
                    setText(item.message());
                    setStyle("-fx-text-fill: #1B5E20; -fx-font-size: 15px;");
                }
            }
        });
    }

    /**
     * Rebuilds list of people currently present in room.
     */
    private void updatePeopleInRoom() {
        peopleBox.getChildren().clear();

        if (GameHandler.game.getCurrentRoom() == null) {
            return;
        }

        java.util.List<Person> people = GameHandler.game.getCurrentRoom().getOtherPeople();
        if (people == null || people.isEmpty()) {
            Label emptyPeopleLabel = new Label("Nikdo v místnosti není");
            emptyPeopleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
            peopleBox.getChildren().add(emptyPeopleLabel);
            return;
        }

        for (Person person : people) {
            Button personButton = new Button(person.toString());
            personButton.setMaxWidth(Double.MAX_VALUE);
            if (!person.isAlive()) {
                personButton.setStyle("-fx-font-size: 14px; -fx-background-color: #ECEFF1; -fx-text-fill: #616161;");
            } else if (!person.isPeaceful()) {
                personButton.setStyle("-fx-font-size: 14px; -fx-background-color: #FFEBEE; -fx-text-fill: #B71C1C; -fx-font-weight: bold;");
            } else {
                personButton.setStyle("-fx-font-size: 14px; -fx-background-color: #E8F5E9; -fx-text-fill: #1B5E20;");
            }
            personButton.setOnAction(event -> interactWithPerson(person));
            peopleBox.getChildren().add(personButton);
        }
    }

    /**
     * Opens interaction choice for clicked person and executes selected command.
     *
     * @param person clicked room person
     */
    private void interactWithPerson(Person person) {
        if (person == null) {
            return;
        }
        if (!person.isAlive()) {
            appendErrorLog("S mrtvou postavou nelze interagovat.");
            return;
        }
        if (!person.isPeaceful()) {
            appendPlayerLog("utok " + person.getName() + " (klik)");
            executeCommandAndLogOutput(("utok " + person.getName()).toLowerCase());
            refreshView();
            return;
        }

        java.util.List<String> options = new java.util.ArrayList<>();
        options.add("Mluvit");
        options.add("Obchodovat");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Mluvit", options);
        dialog.setTitle("Interakce");
        dialog.setHeaderText("Vyber akci pro: " + person.getName());
        dialog.setContentText("Akce:");

        Optional<String> action = dialog.showAndWait();
        if (action.isEmpty()) {
            return;
        }

        if (action.get().equals("Obchodovat")) {
            appendPlayerLog("nakup " + person.getName() + " (klik)");
            trade.openForPerson(person);
        } else {
            appendPlayerLog("mluvit " + person.getName() + " (klik)");
            executeCommandAndLogOutput(("mluvit " + person.getName()).toLowerCase());
        }
        refreshView();
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
        return PlayerNameDialog.requestName(cmdInput == null || cmdInput.getScene() == null ? null : cmdInput.getScene().getWindow());
    }

    /**
     * Asks user for game difficulty in a GUI dialog.
     *
     * @return selected difficulty (default EASY if canceled)
     */
    private Difficulty requestDifficulty() {
        return DifficultyDialog.requestDifficulty(cmdInput == null || cmdInput.getScene() == null ? null : cmdInput.getScene().getWindow());
    }

    /**
     * Updates quest panel so it shows static objectives and removes completed ones.
     */
    private void updateQuestList() {
        setQuestList(QuestTracker.buildQuestText(GameHandler.game, visitedRoomIds));
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
        DIALOG,
        COMBAT_PLAYER,
        COMBAT_ENEMY
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
