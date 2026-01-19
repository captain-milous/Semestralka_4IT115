// File: ui.game.GameHandlerTest.java
package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.logic.space.Difficulty;
import cz.vse.semestralka_4it115.logic.space.Map;
import cz.vse.semestralka_4it115.ui.game.Game;
import cz.vse.semestralka_4it115.ui.game.GameHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameHandlerTest {

    @BeforeEach
    void resetGameInstance() {
        // Reset static game instance before each test
        GameHandler.game = new Game();
    }

    @Test
    void testGetMapReturnsSameInstance() {
        Map firstMap = GameHandler.getMap();
        assertNotNull(firstMap);
        // Modify map via GameHandler.game
        firstMap.setCurrentRoom(null);
        Map secondMap = GameHandler.getMap();
        assertSame(firstMap, secondMap);
    }
}
