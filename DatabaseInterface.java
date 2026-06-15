import java.util.ArrayList;

/**
 * DatabaseInterface.java
 *
 * The interface that is implemented by the Database class
 *
 * @author Timothy Murray, L11
 *
 * @version November 3 2024
 */

public interface DatabaseInterface {

    boolean readUsers();

    boolean addUser(String data) throws UsernameTakenException;

    User searchForUser(String username);

    boolean searchForChatroom(Chatroom chatroom);

    boolean addChatroom(User user1, User user2);

    boolean readChatrooms();

    boolean outputChatrooms();

    boolean outputUsers();

    ArrayList<User> userSearch(String propmpt);

    User[] getRandomUsers();
}