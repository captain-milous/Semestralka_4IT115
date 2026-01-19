package cz.vse.semestralka_4it115.exception;

/**
 * The WinException class handles win exception functionality,
 * thrown when the player achieves victory in the game.
 * Contains the message to display upon winning.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-05-28
 */
public class WinException extends RuntimeException {

    public WinException(String message) {
        super(message);
    }
}
