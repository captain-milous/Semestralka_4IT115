package cz.vse.semestralka_4it115.score;

import cz.vse.semestralka_4it115.logic.space.Difficulty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Immutable game result entry for leaderboard persistence.
 *
 * @param finishedAt end date-time of the run
 * @param playerName player name
 * @param money final player money
 * @param difficulty selected game difficulty
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
