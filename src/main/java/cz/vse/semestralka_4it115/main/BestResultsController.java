package cz.vse.semestralka_4it115.main;

import cz.vse.semestralka_4it115.logic.space.Difficulty;
import cz.vse.semestralka_4it115.score.BestResult;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for best-results table window.
 */
public class BestResultsController {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ObservableList<BestResultRow> rows = FXCollections.observableArrayList();

    @FXML
    private TableView<BestResultRow> resultsTable;
    @FXML
    private TableColumn<BestResultRow, String> dateTimeColumn;
    @FXML
    private TableColumn<BestResultRow, String> playerColumn;
    @FXML
    private TableColumn<BestResultRow, String> moneyColumn;
    @FXML
    private TableColumn<BestResultRow, String> difficultyColumn;

    @FXML
    private void initialize() {
        dateTimeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().finishedAt()));
        playerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().playerName()));
        moneyColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().money()));
        difficultyColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().difficulty()));
        resultsTable.setItems(rows);
    }

    public void setResults(List<BestResult> results) {
        rows.clear();
        if (results == null) {
            return;
        }
        for (BestResult result : results) {
            rows.add(new BestResultRow(
                    result.finishedAt().format(DATE_TIME_FORMATTER),
                    result.playerName(),
                    String.valueOf(result.money()),
                    toLabel(result.difficulty())
            ));
        }
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) resultsTable.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    private String toLabel(Difficulty difficulty) {
        if (difficulty == Difficulty.HARD) {
            return "Tezka";
        }
        if (difficulty == Difficulty.MEDIUM) {
            return "Normalni";
        }
        return "Jednoducha";
    }

    private record BestResultRow(String finishedAt, String playerName, String money, String difficulty) {
    }
}
