import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Database.java
 *
 * The database that holds all the information on the users and messages
 *
 * @author Timothy Murray, L11
 *
 * @version November 3 2024
 */
public class Database implements DatabaseInterface {

    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Chatroom> chatrooms = new ArrayList<>();
    private String usersFile;
    private String chatroomFile;
    private static final Object GATEKEEPER = new Object();

    public Database(String usersFile, String chatroomFile) {
        this.usersFile = usersFile;
        this.chatroomFile = chatroomFile;
    }

    // Reads the users from the users text file and creates them all along with
    // their friended users and blocked users
    // User file is in the format of:
    // Username, password, profile picture
    // Friends:
    // Blocked:
    public boolean readUsers() {
        ArrayList<User> userData = new ArrayList<>();
        try {
            FileReader fr = new FileReader(this.usersFile);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                String username = "";
                String password = "";
                String profilePicture = "";
                String[] data = line.split(";");
                if (data.length == 5) {
                    username = data[0];
                    password = data[1];
                    profilePicture = data[2];
                    User user = new User(username, password, profilePicture);
                    userData.add(user);
                } else if (data.length == 4) {
                    username = data[0];
                    password = data[1];
                    User user = new User(username, password);
                    userData.add(user);
                }
            }
            fr.close();
            br.close();

            FileReader newFR = new FileReader(this.usersFile);
            BufferedReader newBR = new BufferedReader(newFR);
            String newLine = "";

            while ((newLine = newBR.readLine()) != null) {
                String[] newData = newLine.split(";");
                String username;
                String[] friends;
                String[] blocked;
                if (newData.length == 5) {
                    username = newData[0];
                    friends = newData[3].substring(8).split(",");
                    blocked = newData[4].substring(8).split(",");
                    for (int i = 0; i < userData.size(); i++) {
                        if (userData.get(i).getUsername().equals(username)) {
                            for (int j = 0; j < userData.size(); j++) {
                                if (!friends[0].equals("None")) {
                                    for (int k = 0; k < friends.length; k++) {
                                        if (userData.get(j).getUsername().equals(friends[k])) {
                                            userData.get(i).addFriend(userData.get(j));
                                        }
                                    }
                                }
                                if (!blocked[0].equals("None")) {
                                    for (int k = 0; k < blocked.length; k++) {
                                        if (userData.get(j).getUsername().equals(blocked[k])) {
                                            userData.get(i).addBlocked(userData.get(j));
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                } else if (newData.length == 4) {
                    username = newData[0];
                    friends = newData[2].substring(8).split(",");
                    blocked = newData[3].substring(8).split(",");
                    for (int i = 0; i < userData.size(); i++) {
                        if (userData.get(i).getUsername().equals(username)) {
                            for (int j = 0; j < userData.size(); j++) {
                                if (!friends[0].equals("None")) {
                                    for (int k = 0; k < friends.length; k++) {
                                        if (userData.get(j).getUsername().equals(friends[k])) {
                                            userData.get(i).addFriend(userData.get(j));
                                        }
                                    }
                                }
                                if (!blocked[0].equals("None")) {
                                    for (int k = 0; k < blocked.length; k++) {
                                        if (userData.get(j).getUsername().equals(blocked[k])) {
                                            userData.get(i).addBlocked(userData.get(j));
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
            br.close();
            synchronized (GATEKEEPER) {
                users = userData;
            }
            newBR.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
            return false;
        }
    }

    // When a new user is created upon account creation, their information is stored
    // in the database
    public boolean addUser(String data) throws UsernameTakenException {
        User newUser = null;
        String[] newData = data.split(";");
        if (newData.length == 3) {
            newUser = new User(newData[0], newData[1], newData[2]);
        } else {
            newUser = new User(newData[0], newData[1]);
        }

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(newUser.getUsername())) {
                throw new UsernameTakenException("Username already taken");
            }
        }
        synchronized (GATEKEEPER) {
            users.add(newUser);
        }
        return true;
    }

    // Finds a specific user in the database, if it doesn't exist it returns null
    public User searchForUser(String username) {
        synchronized (GATEKEEPER) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUsername().equals(username)) {
                    return users.get(i);
                }
            }
        }
        return null;
    }

    // Reads the chatroom file and creates the chatrooms with the messages that were
    // sent in them upon creation
    // A chatroom is between 2 users
    // Chatroom file is formatted as:
    // User:
    // User:
    // message,image,to,from
    public boolean readChatrooms() {
        Chatroom currentChatroom = null;
        User user1 = null;
        User user2 = null;
        int count = 0;
        try {
            FileReader fr = new FileReader(this.chatroomFile);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",;,");
                if (data[0].contains("User:") && count < 2) {
                    if (count == 0) {
                        if (data.length == 2) {
                            user1 = new User(data[0].substring(5), data[1]);
                        } else if (data.length == 3) {
                            user1 = new User(data[0].substring(5), data[1], data[2]);
                        }
                        count++;
                    } else if (count == 1) {
                        if (data.length == 2) {
                            user2 = new User(data[0].substring(5), data[1]);
                        } else if (data.length == 3) {
                            user2 = new User(data[0].substring(5), data[1], data[2]);
                        }
                        currentChatroom = new Chatroom(user1, user2);
                        synchronized (GATEKEEPER) {
                            chatrooms.add(currentChatroom);
                        }
                        count = 0;
                    }
                } else {
                    if (data.length == 4 && currentChatroom != null) {
                        currentChatroom.addMessage(data[0], data[1], data[2], data[3]);
                    } else if (data.length == 3 && currentChatroom != null) {
                        currentChatroom.addMessage(data[0], data[1], data[2]);
                    }
                }
            }
            br.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
            return false;
        }
    }

    // When a user starts a message with another user a chatroom is created between
    // the two
    public boolean addChatroom(User user1, User user2) {
        try {
            synchronized (GATEKEEPER) {
                chatrooms.add(new Chatroom(user1, user2));
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    // Searches the database to see if a chatroom already exists between two users
    public boolean searchForChatroom(Chatroom chatroom) {
        synchronized (GATEKEEPER) {
            for (int i = 0; i < chatrooms.size(); i++) {
                if (chatrooms.get(i).equals(chatroom)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean searchForChatroom(User one, User two) {
        synchronized (GATEKEEPER) {
            for (int i = 0; i < chatrooms.size(); i++) {
                if (chatrooms.get(i).getUser1().equals(one) && chatrooms.get(i).getUser2().equals(one) ||
                        chatrooms.get(i).getUser1().equals(two) && chatrooms.get(i).getUser2().equals(two)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Chatroom getChatroom(User one, User two) {
        synchronized (GATEKEEPER) {
            for (int i = 0; i < chatrooms.size(); i++) {
                if (chatrooms.get(i).getUser1().equals(one) || chatrooms.get(i).getUser2().equals(one) &&
                        chatrooms.get(i).getUser1().equals(two) || chatrooms.get(i).getUser2().equals(two)) {
                    return chatrooms.get(i);
                }
            }
        }
        return null;
    }

    // Outputs the chat room in this format so it can easily be read back into the
    // client:
    // User:
    // User:
    // message,image,to,from
    public boolean outputChatrooms() {
        try {
            FileWriter fr = new FileWriter(this.chatroomFile);
            BufferedWriter writer = new BufferedWriter(fr);
            synchronized (GATEKEEPER) {
                for (int i = 0; i < chatrooms.size(); i++) {
                    if (chatrooms.get(i).getUser1().getProfileImage().equals("default.png")) {
                        writer.write("User:" + chatrooms.get(i).getUser1().getUsername()
                                + ",;," + chatrooms.get(i).getUser1().getPassword());
                        writer.newLine();
                        if (chatrooms.get(i).getUser2().getProfileImage().equals("default.png")) {
                            writer.write("User:" + chatrooms.get(i).getUser2().getUsername()
                                    + ",;," + chatrooms.get(i).getUser2().getPassword());
                            writer.newLine();
                        } else {
                            writer.write("User:" + chatrooms.get(i).getUser2().getUsername()
                                    + ",;," + chatrooms.get(i).getUser2().getPassword()
                                    + ",;," + chatrooms.get(i).getUser2().getProfileImage());
                            writer.newLine();
                        }
                        for (int j = 0; j < chatrooms.get(i).getMessages().size(); j++) {
                            writer.write(chatrooms.get(i).getMessages().get(j).toString());
                            writer.newLine();
                        }
                    } else if (!chatrooms.get(i).getUser1().getProfileImage().equals("default.png")) {
                        writer.write("User:" + chatrooms.get(i).getUser1().getUsername()
                                + ",;," + chatrooms.get(i).getUser1().getPassword()
                                + ",;," + chatrooms.get(i).getUser1().getProfileImage());
                        writer.newLine();
                        if (chatrooms.get(i).getUser2().getProfileImage() == null) {
                            writer.write("User:" + chatrooms.get(i).getUser2().getUsername()
                                    + ",;," + chatrooms.get(i).getUser2().getPassword());
                            writer.newLine();
                        } else {
                            writer.write("User:" + chatrooms.get(i).getUser2().getUsername()
                                    + ",;," + chatrooms.get(i).getUser2().getPassword()
                                    + ",;," + chatrooms.get(i).getUser2().getProfileImage());
                            writer.newLine();
                        }
                        for (int j = 0; j < chatrooms.get(i).getMessages().size(); j++) {
                            writer.write(chatrooms.get(i).getMessages().get(j).toString());
                            writer.newLine();
                        }
                    }
                    writer.flush();
                }
                writer.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Outputs the users in this format so it can easily be read back into the
    // client and no user is lost:
    // Username, password, profile picture
    // Friends:
    // Blocked:
    public boolean outputUsers() {
        try {
            FileWriter fr = new FileWriter(this.usersFile);
            BufferedWriter writer = new BufferedWriter(fr);
            synchronized (GATEKEEPER) {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getProfileImage().equals("\"default.png\"")) {
                        writer.write(users.get(i).getUsername() + ";" + users.get(i).getPassword());
                    } else {
                        writer.write(users.get(i).getUsername()
                                + ";" + users.get(i).getPassword() + ";" + users.get(i).getProfileImage());
                    }
                    String friends = "";
                    for (int j = 0; j < users.get(i).getFriends().size(); j++) {
                        if (!users.get(i).getFriends().isEmpty()) {
                            friends += users.get(i).getFriends().get(j).getUsername() + ",";
                        }
                    }
                    if (friends.isEmpty()) {
                        writer.write(";Friends:None;");
                    } else {
                        friends = friends.substring(0, friends.length() - 1);
                        writer.write(";Friends:" + friends + ";");
                    }
                    String blocked = "";
                    for (int j = 0; j < users.get(i).getBlocked().size(); j++) {
                        if (!users.get(i).getBlocked().isEmpty()) {
                            blocked += users.get(i).getBlocked().get(j).getUsername() + ",";
                        }
                    }
                    if (blocked.isEmpty()) {
                        writer.write("Blocked:None");
                    } else {
                        blocked = blocked.substring(0, blocked.length() - 1);
                        writer.write("Blocked:" + blocked);
                    }
                    writer.newLine();
                }
                writer.flush();
            }
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<User> userSearch(String prompt) {
        ArrayList<User> results = new ArrayList<User>();
        synchronized (GATEKEEPER) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i) != null && users.get(i).getUsername().contains(prompt)) {
                    results.add(users.get(i));
                }
            }
        }
        return results;
    }

    public User[] getRandomUsers() {
        User[] results = new User[6];
        boolean[] used = new boolean[users.size()];
        Random r = new Random();
        int cap = users.size(); // # of valid users
        synchronized (GATEKEEPER) {
            for (int i = 0; i < results.length && i < cap; i++) {
                int t = r.nextInt(users.size());
                if (used[t] || users.get(t) == null) {
                    i--;
                    if (users.get(t) == null) {
                        cap--;
                    }
                } else {
                    results[i] = users.get(t);
                }
            }
        }
        return results;
    }

    // public static void main(String[] args) {
        // Database db = new Database("userInfo.txt", "chatroom.txt");
        // db.readUsers();
        // db.readChatrooms();
        // db.outputUsers();
        // db.outputChatrooms();
        // System.out.println(chatrooms);
    //
    // }
}
