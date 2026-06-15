/**
 * 
 * Client Handler Interface
 * 
 * Inteface for ClientHandler class
 * 
 * @author Purdue CS: Shreyas Aryah
 * 
 * @version November 16, 2024
 */

public interface ClientHandlerInterface {
    String login(String username, String password);

    String createAccount(String username, String password);

    String addFriend(String username, String friendUsername);

    String sendMessage(String toUser, String fromUser, String message);
}
