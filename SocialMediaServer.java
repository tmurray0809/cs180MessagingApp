import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * SocialMediaServer.java
 *
 * The server
 *
 * @author Timothy Murray, L11
 *
 * @version December 8 2024
 */
public class SocialMediaServer implements Runnable, SocialMediaServerInterface {

    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    public static Database database = new Database("userInfo.txt", "chatroom.txt");
    Socket socket;

    public SocialMediaServer(Socket socket) throws IOException {
        this.socket = socket;
    }

    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while (true) {
                try {
                    Task command = (Task) ois.readObject();
                    switch (command.getTask()) {
                        case "Login":
                            User user = (User) ois.readObject();
                            User userTemp = database.searchForUser(user.getUsername());
                            if (user.getUsername().equals(userTemp.getUsername())
                                    && user.getPassword().equals(userTemp.getPassword())) {
                                oos.writeObject(new Task("Success"));
                                oos.flush();
                                oos.writeObject(userTemp);
                                oos.flush();
                            } else {
                                oos.writeObject(new Task("Failure"));
                                oos.flush();
                            }
                            break;
                        case "Register":
                            User user1 = (User) ois.readObject();
                            User user1Temp = database.searchForUser(user1.getUsername());
                            if (user1Temp == null) {
                                oos.writeObject(new Task("Success"));
                                oos.flush();
                                try {
                                    database.addUser(user1.getUsername() + ";" + user1.getPassword());
                                    oos.writeObject(database.searchForUser(user1.getUsername()));
                                    oos.flush();
                                } catch (UsernameTakenException e) {
                                    oos.writeObject(new Task("Username taken"));
                                    oos.flush();
                                }
                            } else {
                                oos.writeObject(new Task("Failure"));
                                oos.flush();
                            }
                            database.outputUsers();
                            break;
                        case "ViewProfile":
                            User user2 = (User) ois.readObject();
                            oos.writeObject(new Task(Integer.toString(user2.getFriends().size())));
                            oos.flush();
                            for (int i = 0; i < user2.getFriends().size(); i++) {
                                oos.writeObject(user2.getFriends().get(i));
                                oos.flush();
                            }

                            oos.writeObject(new Task(Integer.toString(user2.getBlocked().size())));
                            oos.flush();
                            for (int i = 0; i < user2.getBlocked().size(); i++) {
                                oos.writeObject(user2.getBlocked().get(i));
                                oos.flush();
                            }
                            break;
                        case "RemoveFriend":
                            Task task44 = (Task) ois.readObject();
                            User selectedFriend = database.searchForUser(task44.getTask());
                            User user3 = (User) ois.readObject();
                            User u3 = database.searchForUser(user3.getUsername());
                            u3.removeFriend(selectedFriend);
                            user3.removeFriend(selectedFriend);
                            oos.writeObject(user3);
                            oos.flush();
                            database.outputUsers();
                            break;
                        case "BlockFriend":
                            Task tempUser = (Task) ois.readObject();
                            User tempUserReal = database.searchForUser(tempUser.getTask());
                            User user4 = (User) ois.readObject();
                            User u4 = database.searchForUser(user4.getUsername());
                            u4.removeFriend(tempUserReal);
                            u4.addFriend(tempUserReal);
                            user4.removeFriend(tempUserReal);
                            user4.addBlocked(tempUserReal);
                            oos.writeObject(user4);
                            oos.flush();
                            database.outputUsers();
                            break;
                        case "Unblock":
                            Task tempUser1 = (Task) ois.readObject();
                            User tempUser1Real = database.searchForUser(tempUser1.getTask());
                            User user5 = (User) ois.readObject();
                            User u5 = database.searchForUser(user5.getUsername());
                            u5.removeBlocked(tempUser1Real);
                            user5.removeBlocked(tempUser1Real);
                            oos.writeObject(user5);
                            oos.flush();
                            database.outputUsers();
                            break;
                        case "MutualFriends":
                            User user6 = (User) ois.readObject();
                            User u6 = database.searchForUser(user6.getUsername());
                            Task tempUser2 = (Task) ois.readObject();
                            User tempUser2Real = database.searchForUser(tempUser2.getTask());
                            ArrayList<User> friends1 = u6.getFriends();
                            ArrayList<User> friends2 = tempUser2Real.getFriends();
                            ArrayList<String> mutual = new ArrayList<>();
                            for (int i = 0; i < friends1.size(); i++) {
                                for (int j = 0; j < friends2.size(); j++) {
                                    if (friends1.get(i).getUsername().equals(friends2.get(j).getUsername())) {
                                        mutual.add(friends1.get(i).getUsername());
                                        break;
                                    }
                                }
                            }
                            oos.writeObject(new Task(Integer.toString(mutual.size())));
                            oos.flush();
                            for (int i = 0; i < mutual.size(); i++) {
                                oos.writeObject(new Task(mutual.get(i)));
                            }
                            break;
                        case "ChangeUsername":
                            Task tempTask = (Task) ois.readObject();
                            User tempUser3 = database.searchForUser(tempTask.getTask());
                            User user8 = (User) ois.readObject();
                            User u8 = database.searchForUser(user8.getUsername());
                            if (tempUser3 == null) {
                                u8.setUsername(user8.getUsername());
                                user8.setUsername(tempTask.getTask());
                                oos.writeObject(user8);
                                oos.flush();
                            }
                            database.outputUsers();
                            break;
                        case "ChangePassword":
                            Task newPassword = (Task) ois.readObject();
                            Task oldPassword = (Task) ois.readObject();
                            User user7 = (User) ois.readObject();
                            User u7 = database.searchForUser(user7.getUsername());
                            if (user7.getPassword().equals(oldPassword.getTask())) {
                                oos.writeObject(new Task("Success"));
                                oos.flush();
                                u7.setPassword(newPassword.getTask());
                                user7.setPassword(newPassword.getTask());
                                oos.writeObject(user7);
                                oos.flush();
                            } else {
                                oos.writeObject(new Task("Failure"));
                                oos.flush();
                            }
                            database.outputUsers();
                            break;
                        case "AddFriend":
                            Task temp4 = (Task) ois.readObject();
                            User tempUser4 = database.searchForUser(temp4.getTask());
                            User user11 = (User) ois.readObject();
                            User u11 = database.searchForUser(user11.getUsername());
                            boolean isValid = true;
                            for (int i = 0; i < user11.getFriends().size(); i++) {
                                if (user11.getFriends().get(i).getUsername().equals(tempUser4.getUsername())) {
                                    isValid = false;
                                    break;
                                }
                            }
                            for (int i = 0; i < user11.getBlocked().size(); i++) {
                                if (user11.getBlocked().get(i).getUsername().equals(tempUser4.getUsername())) {
                                    isValid = false;
                                    break;
                                }
                            }
                            if (isValid) {
                                u11.addFriend(tempUser4);
                                user11.addFriend(tempUser4);
                                oos.writeObject(new Task("Success"));
                                oos.flush();
                                oos.writeObject(user11);
                                oos.flush();
                            } else {
                                oos.writeObject(new Task("Failure"));
                                oos.flush();
                            }
                            database.outputUsers();
                            break;
                        case "SearchForUser":
                            Task temp6 = (Task) ois.readObject();
                            User tempUser6 = database.searchForUser(temp6.getTask());
                            User user12 = (User) ois.readObject();
                            ArrayList<User> searchedForUsers = database.userSearch(temp6.getTask());
                            if (user12.getBlocked() != null) {
                                for (int i = 0; i < user12.getBlocked().size(); i++) {
                                    for (int j = 0; j < searchedForUsers.size(); j++) {
                                        if (user12.getBlocked().get(i).getUsername().equals(temp6.getTask())) {
                                            searchedForUsers.remove(j);
                                            break;
                                        }
                                    }
                                }
                                String size = Integer.toString(searchedForUsers.size());
                                oos.writeObject(new Task(size));
                                oos.flush();
                                for (int i = 0; i < searchedForUsers.size(); i++) {
                                    oos.writeObject(new Task(searchedForUsers.get(i).getUsername()));
                                    oos.flush();
                                }
                            } else {
                                String size = Integer.toString(searchedForUsers.size());
                                oos.writeObject(new Task(size));
                                oos.flush();
                                for (int i = 0; i < searchedForUsers.size(); i++) {
                                    oos.writeObject(new Task(searchedForUsers.get(i).getUsername()));
                                    oos.flush();
                                }
                            }
                            break;
                        case "SuggestedFriends":
                            User user10 = (User) ois.readObject();
                            User[] suggUsers = database.getRandomUsers();
                            ArrayList<User> tempUserBlocked = user10.getBlocked();
                            ArrayList<User> tempUserFriends = user10.getFriends();
                            if (tempUserBlocked != null) {
                                for (int i = 0; i < tempUserBlocked.size(); i++) {
                                    for (int j = 0; j < suggUsers.length; j++) {
                                        if (suggUsers[j] != null && tempUserBlocked.get(i).getUsername()
                                                .equals(suggUsers[j].getUsername())) {
                                            suggUsers[j] = null;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (tempUserFriends != null) {
                                for (int i = 0; i < tempUserFriends.size(); i++) {
                                    for (int j = 0; j < suggUsers.length; j++) {
                                        if (suggUsers[j] != null && tempUserFriends.get(i).getUsername()
                                                .equals(suggUsers[j].getUsername())) {
                                            suggUsers[j] = null;
                                            break;
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < suggUsers.length; i++) {
                                if (suggUsers[i] != null && user10.getUsername().equals(suggUsers[i].getUsername())) {
                                    suggUsers[i] = null;
                                }
                            }
                            ArrayList<String> suggestedFriends = new ArrayList<>();
                            for (int i = 0; i < suggUsers.length; i++) {
                                if (suggUsers[i] != null) {
                                    suggestedFriends.add(suggUsers[i].getUsername());
                                }
                            }
                            if (!suggestedFriends.isEmpty()) {
                                oos.writeObject(new Task(Integer.toString(suggestedFriends.size())));
                                oos.flush();
                            } else {
                                oos.writeObject(new Task("0"));
                                oos.flush();
                            }
                            for (int i = 0; i < suggestedFriends.size(); i++) {
                                oos.writeObject(new Task(suggestedFriends.get(i)));
                                oos.flush();
                            }

                            if (tempUserFriends == null && tempUserBlocked == null) {
                                if (suggUsers.length > 0) {
                                    oos.writeObject(new Task(Integer.toString(suggUsers.length)));
                                    oos.flush();
                                } else {
                                    oos.writeObject(new Task("0"));
                                    oos.flush();
                                }
                                for (int i = 0; i < suggUsers.length; i++) {
                                    if (suggUsers[i] != null) {
                                        oos.writeObject(new Task(suggUsers[i].getUsername()));
                                        oos.flush();
                                    }
                                }
                            }
                            break;
                        case "BlockUser":
                            Task temp5 = (Task) ois.readObject();
                            User userToBlock = database.searchForUser(temp5.getTask());
                            User user13 = (User) ois.readObject();
                            User u13 = database.searchForUser(user13.getUsername());
                            u13.addBlocked(userToBlock);
                            user13.addBlocked(userToBlock);
                            oos.writeObject(user13);
                            oos.flush();
                            database.outputUsers();
                            break;
                        case "LoadMessages":
                            Task temp8 = (Task) ois.readObject();
                            User realTempUser = database.searchForUser(temp8.getTask());
                            User us = (User) ois.readObject();

                            Chatroom room = database.getChatroom(realTempUser, us);
                            if (room != null && !room.getMessages().isEmpty()) {
                                oos.writeObject(new Task(Integer.toString(room.getMessages().size())));
                                oos.flush();
                                ArrayList<String> msgs = new ArrayList<>();
                                for (int i = 0; i < room.getMessages().size(); i++) {
                                    msgs.add(room.getMessages().get(i).toString());
                                }
                                for (int i = 0; i < msgs.size(); i++) {
                                    String[] tempMsgs = new String[3];
                                    tempMsgs = msgs.get(i).split(",;,");
                                    if (tempMsgs[1].equals(temp8.getTask()) && tempMsgs[2].equals(us.getUsername())) {
                                        oos.writeObject(new Task("To: " + tempMsgs[0]));
                                        oos.flush();
                                    }
                                    if (tempMsgs[1].equals(us.getUsername()) && tempMsgs[2].equals(temp8.getTask())) {
                                        oos.writeObject(new Task("From: " + tempMsgs[0]));
                                        oos.flush();
                                    }
                                }
                            } else {
                                oos.writeObject(new Task("0"));
                                oos.flush();
                                database.addChatroom(realTempUser, us);
                            }
                            database.outputChatrooms();
                            break;
                        case "SendMessage":
                            Task message = (Task) ois.readObject();
                            Task toUserr = (Task) ois.readObject();
                            User toUser = database.searchForUser(toUserr.getTask());
                            User fromUser = (User) ois.readObject();
                            Chatroom chatroom = database.getChatroom(toUser, fromUser);
                            chatroom.addMessage(message.getTask(), toUser.getUsername(), fromUser.getUsername());
                            database.outputChatrooms();
                            break;
                        case "DeleteMessage":
                            Task messageToDelete = (Task) ois.readObject();
                            User userOne = (User) ois.readObject();
                            Task uT = (Task) ois.readObject();
                            User userTwo = database.searchForUser(uT.getTask());
                            Message m = new Message(messageToDelete.getTask(), userOne.getUsername(),
                                    userTwo.getUsername());
                            Chatroom chatroomOne = database.getChatroom(userOne, userTwo);
                            chatroomOne.deleteMessage(m);
                            database.outputChatrooms();
                            break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        database.readUsers();
        database.readChatrooms();
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                SocialMediaServer socialMediaServer = new SocialMediaServer(socket);
                new Thread(socialMediaServer).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
