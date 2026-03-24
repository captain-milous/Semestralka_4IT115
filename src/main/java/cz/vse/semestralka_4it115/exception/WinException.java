package cz.vse.semestralka_4it115.exception;

/**
 * The WinException class handles win exception functionality,
 * thrown when the player achieves victory in the game.
 * Contains the message to display upon winning.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public class WinException extends RuntimeException {

    /**
     * Creates win exception with message shown to player.
     *
     * @param message win message
     */
    public WinException(String message) {
        super(message);
    }
}
