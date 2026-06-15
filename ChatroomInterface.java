import java.util.ArrayList;

/**
 * ChatroomInterface.java
 *
 * The interface that is implemented by the Chatroom class
 *
 * @author Timothy Murray, L11
 *
 * @version November 3 2024
 */
public interface ChatroomInterface {

    ArrayList<Message> getMessages();

    void addMessage(String message, String image, String toUser, String fromUser);

    void addMessage(String message, String toUser, String fromUser);

    boolean deleteMessage(Message message);

    boolean editMessage(Message message, String newMessage);

    boolean equals(Object o);

    User getUser1();

    User getUser2();
}
