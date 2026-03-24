package cz.vse.semestralka_4it115.main;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.item.Item;
import cz.vse.semestralka_4it115.ui.game.ShopUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Consumer;

/**
 * Controls the trade dialog UI and executes buy/sell actions between player and dealer.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class TradeController {
    @FXML
    private Label dealerNameLabel;
    @FXML
    private Label playerMoneyLabel;
    @FXML
    private Label dealerMoneyLabel;
    @FXML
    private ListView<Item> playerItemsList;
    @FXML
    private ListView<Item> dealerItemsList;
    @FXML
    private Button closeButton;

    private Person player;
    private Person dealer;
    private Runnable refreshViewAction;
    private Consumer<String> gameLog;
    private Consumer<String> errorLog;

    /**
     * Creates trade dialog controller used by JavaFX FXML loader.
     */
    public TradeController() {
    }

    /**
     * Configures list cell renderers for both trade item lists.
     */
    @FXML
    private void initialize() {
        playerItemsList.setCellFactory(list -> new ItemCell());
        dealerItemsList.setCellFactory(list -> new ItemCell());
    }

    /**
     * Initializes controller with current participants and log callbacks.
     *
     * @param player active player
     * @param dealer selected dealer
     * @param refreshViewAction callback to refresh main window
     * @param gameLog callback for normal game output
     * @param errorLog callback for error output
     */
    public void init(
            Person player,
            Person dealer,
            Runnable refreshViewAction,
            Consumer<String> gameLog,
            Consumer<String> errorLog
    ) {
        this.player = player;
        this.dealer = dealer;
        this.refreshViewAction = refreshViewAction;
        this.gameLog = gameLog;
        this.errorLog = errorLog;

        dealerNameLabel.setText(dealer.getName());
        refreshTradeWindowData();
    }

    /**
     * Handles selling the currently selected player item to dealer.
     */
    @FXML
    private void onSellClicked() {
        Item selected = playerItemsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLog.accept("Vyberte polozku z vaseho inventare.");
            return;
        }
        try {
            ShopUI.transferItemWithMoney(player, dealer, selected);
            gameLog.accept("Prodal jsi " + selected.getName() + " za " + selected.getValue() + " korun.");
            refreshTradeWindowData();
            refreshViewAction.run();
        } catch (Exception e) {
            errorLog.accept(e.getMessage());
        }
    }

    /**
     * Handles buying the currently selected dealer item for player.
     */
    @FXML
    private void onBuyClicked() {
        Item selected = dealerItemsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLog.accept("Vyberte polozku od obchodnika.");
            return;
        }
        try {
            ShopUI.transferItemWithMoney(dealer, player, selected);
            gameLog.accept("Koupil jsi " + selected.getName() + " za " + selected.getValue() + " korun.");
            refreshTradeWindowData();
            refreshViewAction.run();
        } catch (Exception e) {
            errorLog.accept(e.getMessage());
        }
    }

    /**
     * Closes the trade dialog window.
     */
    @FXML
    private void onCloseClicked() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Refreshes money labels and both item lists after trade operation.
     */
    private void refreshTradeWindowData() {
        playerMoneyLabel.setText("Peníze: " + player.getMoney() + " Kč");
        dealerMoneyLabel.setText("Peníze: " + dealer.getMoney() + " Kč");
        playerItemsList.setItems(mapTradeItems(player.getInventory().getItems()));
        dealerItemsList.setItems(mapTradeItems(dealer.getInventory().getItems()));
    }

    /**
     * Converts item collection into observable list for JavaFX bindings.
     *
     * @param items source items
     * @return observable list used by list views
     */
    private ObservableList<Item> mapTradeItems(List<Item> items) {
        ObservableList<Item> entries = FXCollections.observableArrayList();
        entries.addAll(items);
        return entries;
    }

    private static class ItemCell extends ListCell<Item> {
        /**
         * Renders one row in trade list with name, price, and weight.
         *
         * @param item row item
         * @param empty whether row is empty
         */
        @Override
        protected void updateItem(Item item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                return;
            }
            setText(item.getName() + " | " + item.getValue() + " Kc | " + String.format("%.1f kg", item.getWeight()));
        }
    }
}
