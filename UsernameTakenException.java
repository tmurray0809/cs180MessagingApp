/**
 * UsernameTakenException.java
 *
 * An exception that is thrown when a username already exists
 *
 * @author Timothy Murray, L11
 *
 * @version November 3 2024
 */
public class UsernameTakenException extends Exception implements UsernameTakenExceptionInterface {
    public UsernameTakenException(String message) {
        super(message);
    }
}