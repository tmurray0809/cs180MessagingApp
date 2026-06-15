import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
/**
 * UserProfileFrame.java
 *
 * Where user can see another users profile
 *
 * @author Timothy Murray, L11
 *
 * @version December 8 2024
 */
public class UserProfileFrame extends JComponent implements Runnable {
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    String selectedUser;
    User user;
    JFrame userProfileFrame;
    JButton backButton;
    JLabel profileLabel;
    JLabel mutualFriendsLabel;
    JList<String> mutualFriendsList;
    String[] mutualFriends;
    boolean fromSearch;

    public UserProfileFrame(Socket socket, String selectedUser, User user, ObjectInputStream ois, ObjectOutputStream oos, boolean fromSearch) {
        this.socket = socket;
        this.selectedUser = selectedUser;
        this.user = user;
        this.ois = ois;
        this.oos = oos;
        this.fromSearch = fromSearch;
    }

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == backButton) {
                if (fromSearch) {
                    SwingUtilities.invokeLater(new SearchUserFrame(socket, user, ois, oos));
                    userProfileFrame.dispose();
                } else {
                    SwingUtilities.invokeLater(new ProfileFrame(socket, user, ois, oos));
                    userProfileFrame.dispose();
                }
            }
        }
    };

    public void run() {

        userProfileFrame = new JFrame(selectedUser + "'s profile");
        Container content = userProfileFrame.getContentPane();
        content.setLayout(new BorderLayout());
        content.setBackground(new Color(207, 185, 145));

        userProfileFrame.setSize(800, 600);
        userProfileFrame.setLocationRelativeTo(null);
        userProfileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backButton = new JButton("Back");
        backButton.setBackground(new Color(0, 0, 0));
        backButton.setOpaque(true);
        backButton.setForeground(new Color(196, 191, 192));
        backButton.addActionListener(actionListener);
        content.add(backButton, BorderLayout.NORTH);

        JPanel labelPanel = new JPanel();
        profileLabel = new JLabel("Welcome to " + selectedUser + "'s profile!");
        mutualFriendsLabel = new JLabel("Mutual Friends");
        labelPanel.add(profileLabel);
        labelPanel.add(mutualFriendsLabel);
        content.add(labelPanel, BorderLayout.CENTER);

        try {
            oos.writeObject(new Task("MutualFriends"));
            oos.flush();
            oos.writeObject(user);
            oos.flush();
            oos.writeObject(new Task(selectedUser));
            oos.flush();

            Task temp = (Task) ois.readObject();
            int size = Integer.parseInt(temp.getTask());
            mutualFriends = new String[size];
            for (int i = 0; i < size; i++) {
                Task task = (Task) ois.readObject();
                mutualFriends[i] = task.getTask();
            }
            mutualFriendsList = new JList<>(mutualFriends);
            content.add(mutualFriendsList, BorderLayout.WEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        userProfileFrame.setVisible(true);
    }
}
