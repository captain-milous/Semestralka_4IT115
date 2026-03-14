package cz.vse.semestralka_4it115.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class MainController {
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
    private TextArea systemLog;
    @FXML
    private ImageView map;
    @FXML
    private TextArea questList;

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
        this.systemLog.setText(input);
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
        System.out.println(getCmdInput());
        System.out.println(playerName.getText());

        clearCmdInput();
    }

    /**
     * Clears the cmd input.
     */
    public void clearCmdInput(){
        setCmdInput("");
    }
}
