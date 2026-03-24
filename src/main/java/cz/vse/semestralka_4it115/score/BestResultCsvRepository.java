package cz.vse.semestralka_4it115.score;

import cz.vse.semestralka_4it115.logic.space.Difficulty;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Persists leaderboard entries to CSV and loads them back as {@link BestResult} records.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class BestResultCsvRepository {
    private static final String HEADER = "date_time,player_name,money,difficulty";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path csvPath;

    /**
     * Creates repository using selected CSV file path.
     *
     * @param csvPath path to leaderboard CSV file
     */
    public BestResultCsvRepository(Path csvPath) {
        this.csvPath = Objects.requireNonNull(csvPath, "csvPath must not be null");
    }

    /**
     * Loads all valid leaderboard entries from CSV.
     *
     * @return parsed result list, possibly empty
     * @throws IOException when file cannot be read
     */
    public synchronized List<BestResult> loadAll() throws IOException {
        List<BestResult> results = new ArrayList<>();
        if (!Files.exists(csvPath)) {
            return results;
        }

        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            boolean headerChecked = false;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                if (!headerChecked) {
                    headerChecked = true;
                    if (HEADER.equalsIgnoreCase(line.strip())) {
                        continue;
                    }
                }
                Optional<BestResult> parsedResult = parseRow(line);
                parsedResult.ifPresent(results::add);
            }
        }
        return results;
    }

    /**
     * Rewrites CSV file with provided leaderboard entries.
     *
     * @param results entries to persist
     * @throws IOException when file cannot be written
     */
    public synchronized void saveAll(List<BestResult> results) throws IOException {
        Path parent = csvPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                csvPath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {
            writer.write(HEADER);
            writer.newLine();
            for (BestResult result : results) {
                writer.write(toRow(result));
                writer.newLine();
            }
        }
    }

    private Optional<BestResult> parseRow(String line) {
        List<String> cells = splitCsvRow(line);
        if (cells.size() != 3 && cells.size() != 4) {
            return Optional.empty();
        }

        try {
            LocalDateTime finishedAt = LocalDateTime.parse(cells.get(0), DATE_TIME_FORMATTER);
            String playerName = cells.get(1);
            int money = Integer.parseInt(cells.get(2).strip());
            Difficulty difficulty = cells.size() == 4
                    ? Difficulty.valueOf(cells.get(3).strip())
                    : Difficulty.EASY;
            return Optional.of(new BestResult(finishedAt, playerName, money, difficulty));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    private String toRow(BestResult result) {
        return escapeCsvCell(result.finishedAt().format(DATE_TIME_FORMATTER))
                + ","
                + escapeCsvCell(result.playerName())
                + ","
                + result.money()
                + ","
                + escapeCsvCell(result.difficulty().name());
    }

    private String escapeCsvCell(String value) {
        if (value == null) {
            return "";
        }
        boolean needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        if (!needsQuotes) {
            return value;
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private List<String> splitCsvRow(String row) {
        List<String> cells = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < row.length(); i++) {
            char c = row.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < row.length() && row.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                cells.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        cells.add(current.toString());
        return cells;
    }
}
