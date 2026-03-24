package cz.vse.semestralka_4it115.score;

import cz.vse.semestralka_4it115.logic.space.Difficulty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents one immutable leaderboard entry saved after a finished game.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public record BestResult(LocalDateTime finishedAt, String playerName, int money, Difficulty difficulty) {
    public BestResult {
        finishedAt = Objects.requireNonNull(finishedAt, "finishedAt must not be null");
        playerName = sanitizePlayerName(playerName);
        if (money < 0) {
            money = 0;
        }
        if (difficulty == null) {
            difficulty = Difficulty.EASY;
        }
    }

    /**
     * Creates a result record with current timestamp.
     *
     * @param playerName player name
     * @param money final money
     * @param difficulty game difficulty
     * @return new result entry with current time
     */
    public static BestResult now(String playerName, int money, Difficulty difficulty) {
        return new BestResult(LocalDateTime.now(), playerName, money, difficulty);
    }

    private static String sanitizePlayerName(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            return "Unknown";
        }
        return playerName.strip();
    }
}
