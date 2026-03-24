package cz.vse.semestralka_4it115.exception;

/**
 * Thrown when an action cannot proceed because an enemy is present in the current room.
 * Used to prevent movement or other interactions until the enemy is defeated.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class EnemyException extends RuntimeException {

    public EnemyException(String message) { super(message); }
}
