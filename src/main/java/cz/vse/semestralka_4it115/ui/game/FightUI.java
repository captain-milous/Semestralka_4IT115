package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.logic.space.Difficulty;
import cz.vse.semestralka_4it115.logic.entity.Person;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static cz.vse.semestralka_4it115.ui.game.GameUI.GH;

/**
 * Handles fight interactions between the player and an enemy, managing turn-based combat logic and determining outcomes.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-04-03
 */
public class FightUI {
    private static List<Person> otherPeople;

    /**
     * Initiates a fight with the specified opponent, processing turn-by-turn attacks until one combatant is defeated.
     *
     * @param osoba the identifier (name or index) of the enemy to fight
     * @throws Exception if the enemy cannot be found or an error occurs during combat
     */
    public static void start(String osoba) throws Exception {
        otherPeople = GH.game.getCurrentRoom().getOtherPeople();
        Person player = GH.game.getPlayer();
        Person enemy = findEnemy(osoba);

        if (enemy != null) {
            System.out.println("Souboj mezi " + player.getName() + " a " + enemy.getName() + " započal: ");
            while (true) {
                System.out.println(player.getName() + " zaůtočil.");
                enemy = fight(player, enemy);
                System.out.println(enemy.fullToString() + "\n");
                if (isDead(enemy)) {
                    System.out.println("Výhra hráče!");
                    GH.game.getCurrentRoom().setPersonDead(enemy);
                    break;
                } else {
                    GameUI.wait(200);
                    System.out.println(enemy.getName() + " zaůtočil.");
                    player = fight(enemy, player);
                    System.out.println(player.fullToString() + "\n");
                    if (isDead(player)) {
                        System.out.println("Prohrál jsi souboj!");
                        break;
                    }
                    GameUI.wait(200);
                }
            }
            GH.game.setPlayer(player);
        } else {
            throw new Exception("Protivník nenalezen.");
        }
    }

    /**
     * Finds an enemy in the current room based on input, matching by index or name, and verifies enemy conditions.
     *
     * @param osoba the identifier (name or index) of the enemy to find
     * @return the Person object representing the enemy, or null if not found
     * @throws Exception if the chosen enemy is dead or is peaceful (non-hostile)
     */
    private static Person findEnemy(String osoba) throws Exception {
        Person enemy = null;
        int index = 0;
        try {
            index = Integer.parseInt(osoba);
        } catch (NumberFormatException ignored) {
            index = -1;
        }
        if (index > 0 && index <= otherPeople.size()) {
            try {
                index -= 1;
                if (!otherPeople.get(index).isPeaceful() || otherPeople.get(index).isAlive()) {
                    enemy = otherPeople.get(index);
                    return enemy;
                } else {
                    throw new Exception(otherPeople.get(index).getName() + " je buď mrtvý nebo je mírumilovný.");
                }
            } catch (Exception e) {
                throw new Exception("Nepovedlo se načíst protivníka " + otherPeople.get(index).getName() + ": " + e.getMessage());
            }
        } else if (index == -1) {
            for (Person p : otherPeople) {
                String tName = p.getName().toLowerCase();
                if (osoba.toLowerCase().startsWith(tName)) {
                    try {
                        if (!p.isPeaceful() || p.isAlive()) {
                            enemy = p;
                            return enemy;
                        } else {
                            throw new Exception(p.getName() + " je buď mrtvý nebo je mírumilovný.");
                        }
                    } catch (Exception e) {
                        throw new Exception("Nepovedlo se načíst protivníka " + p.getName());
                    }
                }
            }
        } else {
            throw new Exception("Nevalidní příkaz.");
        }
        return enemy;
    }

    /**
     * Processes a single attack round between attacker and defender, applying damage calculations and special conditions.
     *
     * @param attacker the Person object performing the attack
     * @param defender the Person object receiving the attack
     * @return the updated Person object after damage is applied
     * @throws Exception if the attack is fully blocked or an invalid random event occurs
     */
    private static Person fight(Person attacker, Person defender) throws Exception {
        double aDamage = attacker.getEffectiveStrength();
        double dDefense = defender.getEffectiveDefense();

        if (GH.game.getDifficulty() != Difficulty.EASY) {
            Random rnd = new Random();
            List<Double> rndDamage = Arrays.asList(
                    0.25, 0.5, 0.75,
                    1.0, 1.25, 1.5,
                    1.75, 2.0, 3.0
            );
            List<Double> rndDefense = Arrays.asList(
                    0.5, 1.0, 1.5,
                    2.0, 3.0
            );

            int index = rnd.nextInt(rndDamage.size());
            double bonus = rndDamage.get(index);
            aDamage = aDamage * bonus;
            index = rnd.nextInt(rndDefense.size());
            bonus = rndDefense.get(index);
            if (bonus == 3.0) {
                throw new Exception("Útok byl plně vyblokován.");
            }
            dDefense = dDefense * bonus;
        }
        double damageTaken = (dDefense / 10) - aDamage;
        System.out.println("Síla útoku " + damageTaken);
        if (damageTaken < 0) {
            damageTaken = damageTaken * -1;
            defender.takeDamage(damageTaken);
        }

        return defender;
    }

    /**
     * Determines if the given person is dead.
     *
     * @param p the Person object to check
     * @return true if the person is not alive, false otherwise
     */
    private static boolean isDead(Person p) {
        return !p.isAlive();
    }
}
