package cz.vse.semestralka_4it115.main;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.ui.game.GameHandler;
import cz.vse.semestralka_4it115.ui.game.ShopUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Owns GUI trading flow and opens trade window from FXML.
 */
public class Trade {
    private final Supplier<Window> ownerSupplier;
    private final Runnable refreshViewAction;
    private final Consumer<String> gameLog;
    private final Consumer<String> errorLog;

    public Trade(
            Supplier<Window> ownerSupplier,
            Runnable refreshViewAction,
            Consumer<String> gameLog,
            Consumer<String> errorLog
    ) {
        this.ownerSupplier = ownerSupplier;
        this.refreshViewAction = refreshViewAction;
        this.gameLog = gameLog;
        this.errorLog = errorLog;
    }

    /**
     * Handles "nakup <osoba>" command in GUI mode.
     *
     * @param input full command string
     * @return true when input was a trade command
     */
    public boolean tryHandleTradeCommand(String input) {
        if (input == null) {
            return false;
        }

        String[] tokens = input.trim().split("\\s+");
        if (tokens.length < 2 || !tokens[0].equalsIgnoreCase("nakup")) {
            return false;
        }

        try {
            Person dealer = ShopUI.findDealer(tokens[1], GameHandler.game.getCurrentRoom().getOtherPeople());
            openForPerson(dealer);
        } catch (Exception e) {
            errorLog.accept(e.getMessage());
        }
        return true;
    }

    /**
     * Opens the modal trade window for selected dealer.
     */
    public void openForPerson(Person dealer) {
        if (dealer == null) {
            errorLog.accept("Obchodnik nebyl nalezen.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("trade.fxml"));
            Parent root = loader.load();

            TradeController controller = loader.getController();
            controller.init(GameHandler.game.getPlayer(), dealer, refreshViewAction, gameLog, errorLog);

            Stage stage = new Stage();
            stage.setTitle("Obchod: " + dealer.getName());

            Window owner = ownerSupplier.get();
            if (owner != null) {
                stage.initOwner(owner);
                stage.initModality(Modality.WINDOW_MODAL);
            } else {
                stage.initModality(Modality.APPLICATION_MODAL);
            }

            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            errorLog.accept("Nepovedlo se otevrit obchod: " + e.getMessage());
        }
    }
}
