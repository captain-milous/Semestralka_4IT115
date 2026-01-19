package cz.vse.semestralka_4it115.exception;

/**
 * Thrown when an action cannot proceed because an enemy is present in the current room.
 * Used to prevent movement or other interactions until the enemy is defeated.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-05-28
 */
public class EnemyException extends RuntimeException {

    public EnemyException(String message) { super(message); }
}
