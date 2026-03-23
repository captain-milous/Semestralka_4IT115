package cz.vse.semestralka_4it115.ui.game;

/**
 * Observer for game state updates used by GUI components.
 */
@FunctionalInterface
public interface GameStateObserver {
    /**
     * Invoked when game state changes and the UI should refresh.
     */
    void onGameStateChanged();
}
