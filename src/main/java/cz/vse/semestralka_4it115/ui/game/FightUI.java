package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.item.Item;
import cz.vse.semestralka_4it115.logic.space.Difficulty;
import cz.vse.semestralka_4it115.logic.space.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static cz.vse.semestralka_4it115.ui.game.GameUI.GH;

/**
 * Provides turn-based combat flow for console and GUI battle interactions.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class FightUI {
    private static final int FIGHT_TURN_DELAY_MS = 180;
    private static List<Person> otherPeople;

    /**
     * Callback sink for fight logs.
     */
    public interface FightLogSink {
        void player(String message);

        void enemy(String message);

        void system(String message);
    }

    /**
     * Starts fight in console mode.
     *
     * @param osoba enemy identifier entered by user
     * @throws Exception when enemy cannot be found or combat cannot start
     */
    public static void start(String osoba) throws Exception {
        FightLogSink consoleSink = new FightLogSink() {
            @Override
            public void player(String message) {
                System.out.println(message);
            }

            @Override
            public void enemy(String message) {
                System.out.println(message);
            }

            @Override
            public void system(String message) {
                System.out.println(message);
            }
        };
        start(osoba, consoleSink);
    }

    /**
     * Starts fight and sends combat updates through sink.
     */
    public static void start(String osoba, FightLogSink sink) throws Exception {
        otherPeople = GH.game.getCurrentRoom().getOtherPeople();
        Person player = GH.game.getPlayer();
        Person enemy = findEnemy(osoba);

        if (enemy == null) {
            throw new Exception("Protivnik nenalezen.");
        }

        sink.system("Souboj mezi " + player.getName() + " a " + enemy.getName() + " zapocal.");
        while (true) {
            sink.player(player.getName() + " zautocil.");
            enemy = fight(player, enemy, sink, true);
            sink.system(enemy.fullToString());
            if (isDead(enemy)) {
                sink.system("Vyhral jsi souboj.");
                applyLootAfterKill(player, enemy, GH.game.getCurrentRoom(), sink);
                GH.game.getCurrentRoom().setPersonDead(enemy);
                break;
            }

            GameUI.wait(FIGHT_TURN_DELAY_MS);
            sink.enemy(enemy.getName() + " zautocil.");
            player = fight(enemy, player, sink, false);
            sink.system(player.fullToString());
            if (isDead(player)) {
                sink.system("Prohral jsi souboj.");
                break;
            }
            GameUI.wait(FIGHT_TURN_DELAY_MS);
        }
        GH.game.setPlayer(player);
    }

    private static Person findEnemy(String osoba) throws Exception {
        Person enemy = null;
        int index;
        try {
            index = Integer.parseInt(osoba);
        } catch (NumberFormatException ignored) {
            index = -1;
        }

        if (index > 0 && index <= otherPeople.size()) {
            index -= 1;
            Person candidate = otherPeople.get(index);
            if (!candidate.isPeaceful() && candidate.isAlive()) {
                return candidate;
            }
            throw new Exception(candidate.getName() + " je bud mrtvy nebo je mirumilovny.");
        }

        if (index == -1) {
            for (Person p : otherPeople) {
                if (osoba.toLowerCase().startsWith(p.getName().toLowerCase())) {
                    if (!p.isPeaceful() && p.isAlive()) {
                        return p;
                    }
                    throw new Exception(p.getName() + " je bud mrtvy nebo je mirumilovny.");
                }
            }
        } else {
            throw new Exception("Nevalidni prikaz.");
        }
        return enemy;
    }

    private static Person fight(Person attacker, Person defender, FightLogSink sink, boolean playerAttack) {
        double aDamage = attacker.getEffectiveStrength();
        double dDefense = defender.getEffectiveDefense();

        if (GH.game.getDifficulty() != Difficulty.EASY) {
            Random rnd = new Random();
            List<Double> rndDamage = Arrays.asList(0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 3.0);
            List<Double> rndDefense = Arrays.asList(0.5, 1.0, 1.5, 2.0, 3.0);

            int index = rnd.nextInt(rndDamage.size());
            aDamage = aDamage * rndDamage.get(index);
            index = rnd.nextInt(rndDefense.size());
            double defenseBonus = rndDefense.get(index);
            if (defenseBonus == 3.0) {
                if (playerAttack) {
                    sink.player("Utok byl plne vyblokovan.");
                } else {
                    sink.enemy("Utok byl plne vyblokovan.");
                }
                return defender;
            }
            dDefense = dDefense * defenseBonus;
        }

        double damageTaken = (dDefense / 10) - aDamage;
        if (damageTaken < 0) {
            damageTaken = damageTaken * -1;
            defender.takeDamage(damageTaken);
            String msg = "Zasah za " + String.format("%.1f", damageTaken) + " damage.";
            if (playerAttack) {
                sink.player(msg);
            } else {
                sink.enemy(msg);
            }
        } else {
            if (playerAttack) {
                sink.player("Utok nezpusobil zadne zraneni.");
            } else {
                sink.enemy("Utok nezpusobil zadne zraneni.");
            }
        }
        return defender;
    }

    private static void applyLootAfterKill(Person player, Person enemy, Room room, FightLogSink sink) {
        if (player == null || enemy == null || room == null) {
            return;
        }

        int enemyMoney = enemy.getMoney();
        if (enemyMoney > 0) {
            player.setMoney(player.getMoney() + enemyMoney);
            enemy.setMoney(0);
            sink.system("Ziskal jsi " + enemyMoney + " korun.");
        }

        List<Item> droppedItems = new ArrayList<>(enemy.getInventory().getItems());
        for (Item item : droppedItems) {
            try {
                enemy.dropItemFromInventory(item);
                room.addItem(item);
                sink.system("Na zem vypadl predmet: " + item.getName() + ".");
            } catch (Exception ignored) {
                // Keep combat flow stable even if room item insert fails.
            }
        }
    }

    private static boolean isDead(Person p) {
        return !p.isAlive();
    }
}
