package cz.vse.semestralka_4it115.exception;

/**
 * Thrown when the player dies, typically as a result of losing all health during combat or other events.
 * Carries a message describing the cause of death to be displayed to the user.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class DeathException extends RuntimeException {

    public DeathException(String message) { super(message); }
}
