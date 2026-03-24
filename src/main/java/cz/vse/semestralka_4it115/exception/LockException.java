package cz.vse.semestralka_4it115.exception;

/**
 * Thrown when the player attempts to access a locked room or object without possessing the required key.
 * Contains a message indicating that the lock must be bypassed or the key must be found first.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class LockException extends RuntimeException {

    public LockException(String message) { super(message); }
}
