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
import java.util.ArrayList;

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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * SearchUserFrame.java
 *
 * Where user can search for friends
 *
 * @author Timothy Murray, L11
 *
 * @version December 8 2024
 */
public class SearchUserFrame extends JComponent implements Runnable {
    Socket socket;
    User user;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    JFrame searchUserFrame;
    JList<String> suggestedFriends;
    JList<String> searchedForFriends;
    String[] suggested;
    String[] searched = new String[20];
    JLabel suggestedFriendsLabel;
    JLabel searchedForFriendsLabel;
    JButton searchButton;
    JButton viewProfileButton;
    JButton backButton;
    JPanel searchPanel;
    JPanel suggestedPanel;
    JButton addButton;
    JButton blockButton;
    JTextField searchField;
    String selectedSearch = null;
    String selectedSuggested = null;
    Container content;

    public SearchUserFrame(Socket socket, User user, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.user = user;
        this.ois = ois;
        this.oos = oos;
    }

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == backButton) {
                SwingUtilities.invokeLater(new HomeFrame(socket, user, ois, oos));
                searchUserFrame.dispose();
            }
            if (e.getSource() == addButton && selectedSearch != null) {
                addFriend(selectedSearch);
            }
            if (e.getSource() == addButton && selectedSuggested != null) {
                addFriend(selectedSuggested);
            }
            if (e.getSource() == blockButton && selectedSuggested != null) {
                blockUser(selectedSuggested);
            }
            if (e.getSource() == blockButton && selectedSearch != null) {
                blockUser(selectedSearch);
            }
            if (e.getSource() == searchButton && searchField.getText() != null) {
                performSearch();
            }
            if (e.getSource() == viewProfileButton && selectedSuggested != null) {
                openUserProfile(selectedSuggested);
            }
            if (e.getSource() == viewProfileButton && selectedSearch != null) {
                openUserProfile(selectedSearch);
            }
        }

        private void addFriend(String username) {
            try {
                oos.writeObject(new Task("AddFriend"));
                oos.flush();
                oos.writeObject(new Task(username));
                oos.flush();
                oos.writeObject(user);
                oos.flush();
                Task result = (Task) ois.readObject();
                if (result.getTask().equals("Success")) {
                    user = (User) ois.readObject();
                    JOptionPane.showMessageDialog(null,
                            "Successfully added friend!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Couldn't add friend because you have them added already", "Failed",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        private void blockUser(String username) {
            try {
                oos.writeObject(new Task("BlockUser"));
                oos.flush();
                oos.writeObject(new Task(username));
                oos.flush();
                oos.writeObject(user);
                oos.flush();
                user = (User) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        private void performSearch() {
            try {
                oos.writeObject(new Task("SearchForUser"));
                oos.flush();
                oos.writeObject(new Task(searchField.getText()));
                oos.flush();
                oos.writeObject(user);
                oos.flush();

                ArrayList<String> temp = new ArrayList<>();
                Task tempTask = (Task) ois.readObject();
                int size = Integer.parseInt(tempTask.getTask());
                for (int i = 0; i < size; i++) {
                    Task task = (Task) ois.readObject();
                    temp.add(task.getTask());
                }

                String[] tempArray = temp.toArray(new String[0]);
                searchedForFriends.setListData(tempArray);
                content.repaint();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        private void openUserProfile(String username) {
            SwingUtilities.invokeLater(new UserProfileFrame(socket, username, user, ois, oos, true));
            searchUserFrame.dispose();
        }
    };

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 0, 0));
        button.setOpaque(true);
        button.setForeground(new Color(196, 191, 192));
        button.setMaximumSize(new Dimension(150, 40));
        return button;
    }

    public void run() {
        searchUserFrame = new JFrame("Search Frame");
        content = searchUserFrame.getContentPane();
        content.setLayout(new BorderLayout(10, 10));
        content.setBackground(new Color(207, 185, 145));

        searchUserFrame.setSize(800, 600);
        searchUserFrame.setLocationRelativeTo(null);
        searchUserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel searchUserPanel = new JPanel();
        searchUserPanel.setLayout(new BoxLayout(searchUserPanel, BoxLayout.Y_AXIS));
        searchUserPanel.setBackground(new Color(207, 185, 145));
        searchUserPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(207, 185, 145));
        searchedForFriendsLabel = new JLabel("Search For Friends");
        searchedForFriendsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel searchInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchInputPanel.setBackground(new Color(207, 185, 145));
        searchField = new JTextField(20);
        searchButton = createStyledButton("Search");
        searchButton.addActionListener(actionListener);
        searchInputPanel.add(searchField);
        searchInputPanel.add(searchButton);

        searchedForFriends = new JList<>(searched);
        searchedForFriends.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    selectedSearch = searchedForFriends.getSelectedValue();
                    selectedSuggested = null;
                }
            }
        });

        JScrollPane searchScrollPane = new JScrollPane(searchedForFriends);
        searchScrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 150));

        searchPanel.add(searchedForFriendsLabel, BorderLayout.NORTH);
        searchPanel.add(searchInputPanel, BorderLayout.CENTER);
        searchPanel.add(searchScrollPane, BorderLayout.SOUTH);

        try {
            oos.writeObject(new Task("SuggestedFriends"));
            oos.flush();
            oos.writeObject(user);
            oos.flush();
            Task s = (Task) ois.readObject();
            int size = Integer.parseInt(s.getTask());
            suggested = new String[size];
            for (int i = 0; i < size; i++) {
                Task temp = (Task) ois.readObject();
                suggested[i] = temp.getTask();
            }

            JPanel suggestedPanel = new JPanel(new BorderLayout());
            suggestedPanel.setBackground(new Color(207, 185, 145));
            suggestedFriendsLabel = new JLabel("Suggested Friends");
            suggestedFriendsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            suggestedFriends = new JList<>(suggested);
            suggestedFriends.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        selectedSuggested = suggestedFriends.getSelectedValue();
                        selectedSearch = null;
                    }
                }
            });

            JScrollPane suggestedScrollPane = new JScrollPane(suggestedFriends);
            suggestedScrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));

            suggestedPanel.add(suggestedFriendsLabel, BorderLayout.NORTH);
            suggestedPanel.add(suggestedScrollPane, BorderLayout.CENTER);

            JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            actionButtonPanel.setBackground(new Color(207, 185, 145));

            addButton = createStyledButton("Add User");
            blockButton = createStyledButton("Block User");
            viewProfileButton = createStyledButton("View Profile");

            addButton.addActionListener(actionListener);
            blockButton.addActionListener(actionListener);
            viewProfileButton.addActionListener(actionListener);

            actionButtonPanel.add(addButton);
            actionButtonPanel.add(blockButton);
            actionButtonPanel.add(viewProfileButton);

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

            searchUserPanel.add(searchPanel);
            searchUserPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            searchUserPanel.add(suggestedPanel);
            searchUserPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            searchUserPanel.add(actionButtonPanel);
            searchUserPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            searchUserPanel.add(backButtonPanel);

            content.add(searchUserPanel, BorderLayout.CENTER);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error loading suggested friends",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        searchUserFrame.setVisible(true);
    }
}
