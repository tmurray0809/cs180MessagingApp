import java.util.ArrayList;

/**
 * Chatroom.java
 *
 * The chatroom class that stores messages between 2 users
 *
 * @author Timothy Murray, L11
 *
 * @version November 3 2024
 */
public class Chatroom implements ChatroomInterface {

    private ArrayList<Message> messages;
    private User user1;
    private User user2;

    public Chatroom(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.messages = new ArrayList<>();
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public void addMessage(String message, String image, String toUser, String fromUser) {
        messages.add(new Message(message, image, toUser, fromUser));
    }

    public void addMessage(String message, String toUser, String fromUser) {
        messages.add(new Message(message, toUser, fromUser));
    }

    public boolean deleteMessage(Message message) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).equals(message)) {
                messages.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean editMessage(Message message, String newMessage) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).equals(message)) {
                messages.get(i).setMessage(newMessage);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Chatroom)) {
            return false;
        }

        Chatroom otherChatroom = (Chatroom) o;
        return this.user1.equals(otherChatroom.user1) && this.user2.equals(otherChatroom.user2);
    }
}
