import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * ProfileFrame.java
 *
 * Where user can see their profile
 *
 * @author Timothy Murray, L11
 *
 * @version December 8 2024
 */
public class ProfileFrame extends JComponent implements Runnable {
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    User user;
    JFrame profileFrame;
    JLabel friendsLabel;
    JLabel blockedLabel;
    JList<String> friendsList;
    JList<String> blockedList;
    JButton backButton;
    JButton removeFriendButton;
    JButton blockFriendButton;
    JButton unblockButton;
    JButton viewProfileButton;
    String[] friends;
    String[] blocked;
    String selectedFriend = null;
    String selectedBlocked = null;
    Container content;
    int friendSize;
    int blockedSize;
    JButton sendMessage;

    public ProfileFrame(Socket socket, User user, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.user = user;
        this.ois = ois;
        this.oos = oos;
    }

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == backButton) {
                SwingUtilities.invokeLater(new HomeFrame(socket, user, ois, oos));
                profileFrame.dispose();
            }
            if (selectedFriend != null && e.getSource() == removeFriendButton) {
                try {
                    oos.writeObject(new Task("RemoveFriend"));
                    oos.flush();
                    oos.writeObject(new Task(selectedFriend));
                    oos.flush();
                    oos.writeObject(user);
                    oos.flush();
                    user = (User) ois.readObject();
                    content.repaint();
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, 
                        "Error removing friend", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            if (selectedFriend != null && e.getSource() == blockFriendButton) {
                try {
                    oos.writeObject(new Task("BlockFriend"));
                    oos.flush();
                    oos.writeObject(new Task(selectedFriend));
                    oos.flush();
                    oos.writeObject(user);
                    oos.flush();
                    user = (User) ois.readObject();
                    content.repaint();
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, 
                        "Error blocking friend", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            if (selectedBlocked != null && e.getSource() == unblockButton) {
                try {
                    oos.writeObject(new Task("Unblock"));
                    oos.flush();
                    oos.writeObject(new Task(selectedBlocked));
                    oos.flush();
                    oos.writeObject(user);
                    oos.flush();
                    user = (User) ois.readObject();
                    content.repaint();
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, 
                        "Error unblocking user", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            if ((selectedBlocked != null || selectedFriend != null) && e.getSource() == viewProfileButton) {
                String profileUser = selectedBlocked != null ? selectedBlocked : selectedFriend;
                SwingUtilities.invokeLater(new UserProfileFrame(socket, profileUser, user, ois, oos, false));
                profileFrame.dispose();
            }
            if (selectedFriend != null && e.getSource() == sendMessage) {
                SwingUtilities.invokeLater(new MessagingFrame(socket, selectedFriend, user, ois, oos));
                profileFrame.dispose();
            }
        }
    };

    @Override
    public void run() {
        try {
            profileFrame = new JFrame("My Profile");
            content = profileFrame.getContentPane();
            content.setLayout(new BorderLayout(10, 10));
            content.setBackground(new Color(207, 185, 145));

            profileFrame.setSize(800, 600);
            profileFrame.setLocationRelativeTo(null);
            profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel profilePanel = new JPanel();
            profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
            profilePanel.setBackground(new Color(207, 185, 145));
            profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            oos.writeObject(new Task("ViewProfile"));
            oos.flush();
            oos.writeObject(user);
            oos.flush();

            Task s = (Task) ois.readObject();
            friendSize = Integer.parseInt(s.getTask());
            friends = new String[friendSize];
            for (int i = 0; i < friendSize; i++) {
                User temp = (User) ois.readObject();
                friends[i] = temp.getUsername();
            }

            Task ss = (Task) ois.readObject();
            blockedSize = Integer.parseInt(ss.getTask());
            blocked = new String[blockedSize];
            for (int i = 0; i < blockedSize; i++) {
                User temp = (User) ois.readObject();
                blocked[i] = temp.getUsername();
            }

            JPanel friendsPanel = new JPanel(new BorderLayout());
            friendsPanel.setBackground(new Color(207, 185, 145));
            friendsLabel = new JLabel("Friends:");
            friendsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            friendsList = new JList<>(friends);
            friendsList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        selectedFriend = friendsList.getSelectedValue();
                        selectedBlocked = null;
                    }
                }
            });
            JScrollPane friendsScrollPane = new JScrollPane(friendsList);
            friendsScrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
            friendsPanel.add(friendsLabel, BorderLayout.NORTH);
            friendsPanel.add(friendsScrollPane, BorderLayout.CENTER);

            JPanel blockedPanel = new JPanel(new BorderLayout());
            blockedPanel.setBackground(new Color(207, 185, 145));
            blockedLabel = new JLabel("Blocked:");
            blockedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            blockedList = new JList<>(blocked);
            blockedList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        selectedBlocked = blockedList.getSelectedValue();
                        selectedFriend = null;
                    }
                }
            });
            JScrollPane blockedScrollPane = new JScrollPane(blockedList);
            blockedScrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
            blockedPanel.add(blockedLabel, BorderLayout.NORTH);
            blockedPanel.add(blockedScrollPane, BorderLayout.CENTER);

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
            buttonsPanel.setBackground(new Color(207, 185, 145));

            removeFriendButton = createStyledButton("Remove Friend");
            blockFriendButton = createStyledButton("Block Friend");
            unblockButton = createStyledButton("Unblock");
            viewProfileButton = createStyledButton("View Profile");
            sendMessage = createStyledButton("Send Message");

            removeFriendButton.addActionListener(actionListener);
            blockFriendButton.addActionListener(actionListener);
            unblockButton.addActionListener(actionListener);
            viewProfileButton.addActionListener(actionListener);
            sendMessage.addActionListener(actionListener);

            JPanel backButtonPanel = new JPanel();
            backButtonPanel.setLayout(new BoxLayout(backButtonPanel, BoxLayout.X_AXIS));
            backButtonPanel.setBackground(new Color(207, 185, 145));

            backButton = new JButton("Back");
            backButton.setBackground(new Color(0, 0, 0));
            backButton.setOpaque(true);
            backButton.setForeground(new Color(196, 191, 192));
            backButton.setMaximumSize(new Dimension(100, 40));
            backButton.addActionListener(actionListener);

            backButtonPanel.add(Box.createHorizontalGlue());
            backButtonPanel.add(backButton);
            backButtonPanel.add(Box.createHorizontalGlue());

            profilePanel.add(friendsPanel);
            profilePanel.add(Box.createRigidArea(new Dimension(0, 20)));
            profilePanel.add(blockedPanel);
            profilePanel.add(Box.createRigidArea(new Dimension(0, 20)));

            JPanel actionButtonPanel = new JPanel();
            actionButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            actionButtonPanel.setBackground(new Color(207, 185, 145));
            actionButtonPanel.add(removeFriendButton);
            actionButtonPanel.add(blockFriendButton);
            actionButtonPanel.add(unblockButton);
            actionButtonPanel.add(viewProfileButton);
            actionButtonPanel.add(sendMessage);

            profilePanel.add(actionButtonPanel);
            profilePanel.add(Box.createRigidArea(new Dimension(0, 20)));
            profilePanel.add(backButtonPanel);

            content.add(profilePanel, BorderLayout.CENTER);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error loading profile", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }

        profileFrame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 0, 0));
        button.setOpaque(true);
        button.setForeground(new Color(196, 191, 192));
        button.setMaximumSize(new Dimension(150, 40));
        return button;
    }
}
