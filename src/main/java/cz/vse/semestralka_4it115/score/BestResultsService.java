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
 * Orchestrates leaderboard updates and keeps only best money-based results.
 */
public class BestResultsService {
    private static final int DEFAULT_MAX_RESULTS = 10;
    private static final Comparator<BestResult> BEST_FIRST = Comparator
            .comparingInt(BestResult::money)
            .reversed()
            .thenComparing(BestResult::finishedAt, Comparator.reverseOrder());

    private final BestResultCsvRepository repository;
    private final int maxResults;

    public BestResultsService(BestResultCsvRepository repository, int maxResults) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        if (maxResults < 1) {
            throw new IllegalArgumentException("maxResults must be >= 1");
        }
        this.maxResults = maxResults;
    }

    public static BestResultsService createDefault() {
        return new BestResultsService(
                new BestResultCsvRepository(Path.of("resources", "best-results.csv")),
                DEFAULT_MAX_RESULTS
        );
    }

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
