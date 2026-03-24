package cz.vse.semestralka_4it115.ui.game;

/**
 * Callback contract for notifying GUI components about game state changes.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
@FunctionalInterface
public interface GameStateObserver {
    /**
     * Invoked when game state changes and the UI should refresh.
     */
    void onGameStateChanged();
}
