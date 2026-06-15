import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * HomeFrame.java
 *
 * The home frame of the GUI
 *
 * @author Timothy Murray, L11
 *
 * @version December 8 2024
 */
public class HomeFrame extends JComponent implements Runnable {
    Socket socket;
    User user;
    JFrame homeFrame;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    JLabel welcomeLabel;
    JButton viewProfileButton;
    JButton searchForUserButton;
    JButton editProfileButton;
    JButton logoutButton;

    public HomeFrame(Socket socket, User user, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.user = user;
        this.ois = ois;
        this.oos = oos;
    }

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == viewProfileButton) {
                SwingUtilities.invokeLater(new ProfileFrame(socket, user, ois, oos));
                homeFrame.dispose();
            }
            if (e.getSource() == searchForUserButton) {
                SwingUtilities.invokeLater(new SearchUserFrame(socket, user, ois, oos));
                homeFrame.dispose();
            }
            if (e.getSource() == editProfileButton) {
                SwingUtilities.invokeLater(new EditProfileFrame(socket, user, ois, oos));
                homeFrame.dispose();
            }
        }
    };

    public void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        homeFrame = new JFrame("Home");
        Container content = homeFrame.getContentPane();
        content.setLayout(new BorderLayout(10, 10));
        content.setBackground(new Color(207, 185, 145));

        homeFrame.setSize(800, 600);
        homeFrame.setLocationRelativeTo(null);
        homeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(new Color(207, 185, 145));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        welcomeLabel = new JLabel("Welcome " + user.getUsername() + "!");
        welcomeLabel.setAlignmentX(CENTER_ALIGNMENT);
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(new Color(207, 185, 145));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        viewProfileButton = new JButton("View Your Profile");
        viewProfileButton.setBackground(new Color(0, 0, 0));
        viewProfileButton.setOpaque(true);
        viewProfileButton.setForeground(new Color(196, 191, 192));
        viewProfileButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        viewProfileButton.setAlignmentX(CENTER_ALIGNMENT);
        viewProfileButton.addActionListener(actionListener);
        buttonsPanel.add(viewProfileButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        searchForUserButton = new JButton("Search for User");
        searchForUserButton.setBackground(new Color(0, 0, 0));
        searchForUserButton.setOpaque(true);
        searchForUserButton.setForeground(new Color(196, 191, 192));
        searchForUserButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchForUserButton.setAlignmentX(CENTER_ALIGNMENT);
        searchForUserButton.addActionListener(actionListener);
        buttonsPanel.add(searchForUserButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        editProfileButton = new JButton("Edit Profile");
        editProfileButton.setBackground(new Color(0, 0, 0));
        editProfileButton.setOpaque(true);
        editProfileButton.setForeground(new Color(196, 191, 192));
        editProfileButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        editProfileButton.setAlignmentX(CENTER_ALIGNMENT);
        editProfileButton.addActionListener(actionListener);
        buttonsPanel.add(editProfileButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        content.add(welcomePanel, BorderLayout.NORTH);
        content.add(buttonsPanel, BorderLayout.CENTER);

        homeFrame.setVisible(true);
    }
}
