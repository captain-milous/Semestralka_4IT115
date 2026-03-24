package cz.vse.semestralka_4it115.main;

import cz.vse.semestralka_4it115.logic.space.Room;
import cz.vse.semestralka_4it115.ui.game.Game;

import java.text.Normalizer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Builds quest tracker text from current game progress and visited rooms.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public final class QuestTracker {
    private QuestTracker() {
    }

    /**
     * Creates formatted quest text and hides already completed objectives.
     *
     * @param game current game state
     * @param visitedRoomIds ids of rooms visited during current run
     * @return quest tracker text for GUI panel
     */
    public static String buildQuestText(Game game, Set<Integer> visitedRoomIds) {
        Map<String, Boolean> objectives = new LinkedHashMap<>();
        objectives.put("- Jdi za Králem do trůnního sálu a seber \"Pečeť\"", isTaskOneDone(game));
        objectives.put("- Najdi ve hradě \"Klíč\" k tajné chodbě a odejdi z hradu tajnou chodbou", isTaskTwoDone(game));
        objectives.put("- Zabij nepřítele, které potkáš po cestě a doruč pečeť do druhého hradu", isTaskThreeDone(game, visitedRoomIds));

        StringBuilder sb = new StringBuilder();
        sb.append("Tvé úkoly:\n");
        boolean hasAnyOpenTask = false;

        for (Map.Entry<String, Boolean> objective : objectives.entrySet()) {
            if (!objective.getValue()) {
                sb.append(objective.getKey()).append("\n");
                hasAnyOpenTask = true;
            }
        }

        if (!hasAnyOpenTask) {
            sb.append("- Všechny úkoly jsou splněny.");
        }

        return sb.toString().trim();
    }

    private static boolean isTaskOneDone(Game game) {
        return hasInventoryItem(game, "pečeť");
    }

    private static boolean isTaskTwoDone(Game game) {
        if (!hasInventoryItem(game, "klíč")) {
            return false;
        }
        return game != null && game.getCurrentRoom() != null && game.getCurrentRoom().getId() >= 11;
    }

    private static boolean isTaskThreeDone(Game game, Set<Integer> visitedRoomIds) {
        if (game == null || visitedRoomIds == null || !hasInventoryItem(game, "pečeť")) {
            return false;
        }
        if (game.getCurrentRoom() == null || game.getCurrentRoom().getId() != 15) {
            return false;
        }
        for (Room room : game.getMap().getLayout()) {
            if (!visitedRoomIds.contains(room.getId())) {
                continue;
            }
            if (room.getOtherPeople() == null) {
                continue;
            }
            boolean hasLivingEnemy = room.getOtherPeople().stream()
                    .anyMatch(p -> !p.isPeaceful() && p.isAlive());
            if (hasLivingEnemy) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasInventoryItem(Game game, String expectedPrefix) {
        if (game == null || game.getPlayer() == null || game.getPlayer().getInventory() == null) {
            return false;
        }
        String normalizedPrefix = normalize(expectedPrefix);
        return game.getPlayer().getInventory().getItems().stream()
                .map(item -> normalize(item.getName()))
                .anyMatch(name -> name.startsWith(normalizedPrefix));
    }

    private static String normalize(String value) {
        String noDiacritics = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return noDiacritics.toLowerCase();
    }
}
