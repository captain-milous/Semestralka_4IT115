package cz.vse.semestralka_4it115.score;

import cz.vse.semestralka_4it115.logic.space.Difficulty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies leaderboard sorting, limiting, and CSV persistence behavior.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
class BestResultsServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void recordResultStoresOnlyBestMoneyResultsInSortedOrder() throws Exception {
        Path csvPath = tempDir.resolve("best-results.csv");
        BestResultCsvRepository repository = new BestResultCsvRepository(csvPath);
        BestResultsService service = new BestResultsService(repository, 3);

        service.recordResult("Alice", 120, Difficulty.EASY);
        service.recordResult("Bob", 450, Difficulty.HARD);
        service.recordResult("Cecil", 300, Difficulty.MEDIUM);
        service.recordResult("Dana", 150, Difficulty.EASY);

        List<BestResult> loaded = repository.loadAll();
        assertEquals(3, loaded.size());

        assertEquals("Bob", loaded.get(0).playerName());
        assertEquals(450, loaded.get(0).money());
        assertEquals(Difficulty.HARD, loaded.get(0).difficulty());

        assertEquals("Cecil", loaded.get(1).playerName());
        assertEquals(300, loaded.get(1).money());
        assertEquals(Difficulty.MEDIUM, loaded.get(1).difficulty());

        assertEquals("Dana", loaded.get(2).playerName());
        assertEquals(150, loaded.get(2).money());
        assertEquals(Difficulty.EASY, loaded.get(2).difficulty());
    }

    @Test
    void repositoryRoundTripsCsvEscapedPlayerName() throws Exception {
        Path csvPath = tempDir.resolve("nested").resolve("best-results.csv");
        BestResultCsvRepository repository = new BestResultCsvRepository(csvPath);
        BestResult expected = new BestResult(
                LocalDateTime.of(2026, 3, 23, 10, 30, 0),
                "Karel, \"Lovec\"",
                222,
                Difficulty.HARD
        );

        repository.saveAll(List.of(expected));
        List<BestResult> loaded = repository.loadAll();
        String rawCsv = Files.readString(csvPath, StandardCharsets.UTF_8);

        assertEquals(1, loaded.size());
        assertEquals(expected.finishedAt(), loaded.get(0).finishedAt());
        assertEquals(expected.playerName(), loaded.get(0).playerName());
        assertEquals(expected.money(), loaded.get(0).money());
        assertEquals(expected.difficulty(), loaded.get(0).difficulty());
        assertTrue(rawCsv.contains("\"Karel, \"\"Lovec\"\"\""));
    }

    @Test
    void repositoryLoadsLegacyCsvWithoutDifficultyAsEasy() throws Exception {
        Path csvPath = tempDir.resolve("legacy-best-results.csv");
        String legacyCsv = """
                date_time,player_name,money
                2026-03-23 11:30:00,LegacyPlayer,170
                """;
        Files.writeString(csvPath, legacyCsv, StandardCharsets.UTF_8);

        BestResultCsvRepository repository = new BestResultCsvRepository(csvPath);
        List<BestResult> loaded = repository.loadAll();

        assertEquals(1, loaded.size());
        assertEquals("LegacyPlayer", loaded.get(0).playerName());
        assertEquals(170, loaded.get(0).money());
        assertEquals(Difficulty.EASY, loaded.get(0).difficulty());
    }
}
