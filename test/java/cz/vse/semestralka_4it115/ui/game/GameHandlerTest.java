// File: ui.game.GameHandlerTest.java
package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.logic.space.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class GameHandlerTest {

    @BeforeEach
    void resetGameInstance() {
        // Reset static game instance before each test
        GameHandler.game = new Game();
        GameHandler.clearGameStateObservers();
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

    @Test
    void testGameStateObserverGetsNotifiedAndCanBeRemoved() {
        AtomicInteger notifications = new AtomicInteger(0);
        GameStateObserver observer = notifications::incrementAndGet;

        GameHandler.addGameStateObserver(observer);
        GameHandler.notifyGameStateChanged();
        assertEquals(1, notifications.get());

        GameHandler.removeGameStateObserver(observer);
        GameHandler.notifyGameStateChanged();
        assertEquals(1, notifications.get());
    }
}
