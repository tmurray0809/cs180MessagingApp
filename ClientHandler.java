import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client Handler
 * 
 * Ensures the Server can safely interact with the Client
 * 
 * @author Purdue CS: Shreyas Aryah
 * 
 * @version November 16, 2024
 */

public class ClientHandler implements Runnable, ClientHandlerInterface {

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private User user;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                String action = in.readLine();

                if (action.equals("Login")) {
                    boolean login = false;
                    do {
                        String username = in.readLine();
                        String password = in.readLine();

                        String success = login(username, password);
                        if (success.equals("Success")) {
                            out.write("Success");
                            out.println();
                            out.flush();
                            login = true;
                        } else {
                            out.write("Failed");
                            out.println();
                            out.flush();
                        }
                    } while (!login);
                } else if (action.equals("Create")) {
                    boolean create = false;
                    do {
                        String username = in.readLine();
                        String password = in.readLine();

                        String success = createAccount(username, password);
                        if (success.equals("Success")) {
                            out.write("Success");
                            out.println();
                            out.flush();

                            create = true;
                        } else {
                            out.write("Failed");
                            out.println();
                            out.flush();
                        }
                    } while (!create);
                }

                boolean exit = false;

                do {
                    boolean cont = true;
                    do {
                        String task = in.readLine();
                        switch (task) {
                            case "1":
                                String username = in.readLine();
                                String success = addFriend(user.getUsername(), username);
                                if (success.equals("Success")) {
                                    out.write("Success");
                                    out.println();
                                    out.flush();
                                } else {
                                    out.write("Failed");
                                    out.println();
                                    out.flush();
                                    cont = false;
                                }
                                break;
                            case "2":
                                String friendUsername = in.readLine();
                                User friend = SocialMediaServer.database.searchForUser(friendUsername);
                                if (friend == null) {
                                    out.write("Failed");
                                    out.println();
                                    out.flush();
                                    cont = false;
                                } else {
                                    boolean keepSending = true;
                                    do {
                                        String message = in.readLine();
                                        String send = sendMessage(friendUsername, user.getUsername(), message);
                                        if (send.equals("Success")) {
                                            out.write("Success");
                                            out.println();
                                            out.flush();
                                        } else {
                                            out.write("Failed");
                                            out.println();
                                            out.flush();
                                        }

                                        String response = in.readLine();
                                        if (response.equals("N")) {
                                            keepSending = false;
                                            cont = false;
                                        }
                                    } while (keepSending);
                                }
                                break;
                            default:
                                cont = false;
                                exit = true;
                                break;
                        }
                    } while (cont);
                } while (!exit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String login(String username, String password) {
        User user = new User(username, password);
        if (SocialMediaServer.database.searchForUser(username).equals(user)) {
            this.user = user;
            return "Success";
        }
        return "Error";
    }

    public String createAccount(String username, String password) {
        String data = username + ";" + password;
        try {
            SocialMediaServer.database.addUser(data);
        } catch (UsernameTakenException e) {
            return "Error";
        }
        this.user = user;
        return "Success";
    }

    public String addFriend(String username, String friendUsername) {
        User user = SocialMediaServer.database.searchForUser(username);
        User friend = SocialMediaServer.database.searchForUser(friendUsername);
        if (user == null || friend == null) {
            return "Error";
        }
        user.addFriend(friend);
        return "Success";
    }

    public String sendMessage(String toUser, String fromUser, String message) {
        User to = SocialMediaServer.database.searchForUser(toUser);
        User from = SocialMediaServer.database.searchForUser(fromUser);
        if (to == null || from == null) {
            return "Error";
        }
        if (!SocialMediaServer.database.searchForChatroom(to, from)) {
            SocialMediaServer.database.addChatroom(to, from);
        }
        Chatroom chatroom = SocialMediaServer.database.getChatroom(to, from);
        chatroom.addMessage(message, toUser, fromUser);
        return "Success";
    }
}
