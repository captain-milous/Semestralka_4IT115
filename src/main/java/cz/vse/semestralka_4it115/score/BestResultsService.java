package cz.vse.semestralka_4it115.score;

import cz.vse.semestralka_4it115.logic.space.Difficulty;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Provides leaderboard operations and keeps only the best money-based results.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class BestResultsService {
    private static final int DEFAULT_MAX_RESULTS = 10;
    private static final Comparator<BestResult> BEST_FIRST = Comparator
            .comparingInt(BestResult::money)
            .reversed()
            .thenComparing(BestResult::finishedAt, Comparator.reverseOrder());

    private final BestResultCsvRepository repository;
    private final int maxResults;

    /**
     * Creates leaderboard service with storage backend and size limit.
     *
     * @param repository CSV repository
     * @param maxResults maximum number of kept entries
     */
    public BestResultsService(BestResultCsvRepository repository, int maxResults) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        if (maxResults < 1) {
            throw new IllegalArgumentException("maxResults must be >= 1");
        }
        this.maxResults = maxResults;
    }

    /**
     * Creates default service using {@code resources/best-results.csv}.
     *
     * @return default leaderboard service
     */
    public static BestResultsService createDefault() {
        return new BestResultsService(
                new BestResultCsvRepository(Path.of("resources", "best-results.csv")),
                DEFAULT_MAX_RESULTS
        );
    }

    /**
     * Stores one game result and keeps only top entries by money and finish time.
     *
     * @param playerName player name
     * @param money final money
     * @param difficulty game difficulty
     */
    public synchronized void recordResult(String playerName, int money, Difficulty difficulty) {
        try {
            List<BestResult> bestResults = repository.loadAll();
            bestResults.add(BestResult.now(playerName, money, difficulty));
            bestResults.sort(BEST_FIRST);

            if (bestResults.size() > maxResults) {
                bestResults = new ArrayList<>(bestResults.subList(0, maxResults));
            }

            repository.saveAll(bestResults);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save best results.", e);
        }
    }

    /**
     * Returns current leaderboard sorted from best to worst result.
     *
     * @return leaderboard entries up to configured limit
     */
    public synchronized List<BestResult> getBestResults() {
        try {
            List<BestResult> bestResults = repository.loadAll();
            bestResults.sort(BEST_FIRST);
            if (bestResults.size() > maxResults) {
                return new ArrayList<>(bestResults.subList(0, maxResults));
            }
            return bestResults;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load best results.", e);
        }
    }
}
