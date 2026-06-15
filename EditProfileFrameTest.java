import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * EditProfileFrame.java
 *
 * Frame that allows you to edit your profile
 *
 * @author Timothy Murray, L11
 *
 * @version December 8 2024
 */

public class EditProfileFrameTest extends JComponent implements Runnable {
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    User user;
    JFrame editProfileFrame;
    JLabel changeUsername;
    JLabel confirmOldPassword;
    JLabel changePassword;
    JTextField changeUsernameField;
    JPasswordField confirmOldPasswordField;
    JPasswordField changePasswordField;
    JButton confirmChangeUsername;
    JButton confirmChangePassword;
    JPanel usernamePanel;
    JPanel passwordPanel;
    JButton backButton;

    public EditProfileFrameTest(Socket socket, User user, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.user = user;
        this.ois = ois;
        this.oos = oos;
    }

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == backButton) {
                SwingUtilities.invokeLater(new HomeFrame(socket, user, ois, oos));
                editProfileFrame.dispose();
            }
            if (e.getSource() == confirmChangeUsername) {
                try {
                    oos.writeObject(new Task("ChangeUsername"));
                    oos.flush();
                    oos.writeObject(new Task(changeUsernameField.getText()));
                    oos.flush();
                    oos.writeObject(user);
                    oos.flush();
                    user = (User) ois.readObject();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == confirmChangePassword) {
                try {
                    oos.writeObject(new Task("ChangePassword"));
                    oos.flush();
                    oos.writeObject(new Task(changePasswordField.getText()));
                    oos.flush();
                    oos.writeObject(new Task(confirmOldPasswordField.getText()));
                    oos.flush();
                    oos.writeObject(user);
                    oos.flush();
                    Task result = (Task) ois.readObject();
                    if (result.getTask().equals("Success")) {
                        user = (User) ois.readObject();
                        JOptionPane.showMessageDialog(null,
                                "Successfully changed password", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Error changing password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    };

    public void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        editProfileFrame = new JFrame("Edit Profile");
        Container content = editProfileFrame.getContentPane();
        content.setLayout(new BorderLayout(10, 10));

        ((JPanel) content).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setBackground(Color.WHITE);

        editProfileFrame.setSize(800, 600);
        editProfileFrame.setLocationRelativeTo(null);
        editProfileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backButton = new JButton("← Back");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setFocusPainted(false);
        backButton.addActionListener(actionListener);
        content.add(backButton, BorderLayout.NORTH);

        usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.Y_AXIS));
        usernamePanel.setBorder(BorderFactory.createTitledBorder("Change Username"));
        usernamePanel.setBackground(Color.WHITE);

        changeUsername = new JLabel("Enter new username");
        changeUsernameField = new JTextField(20);
        changeUsernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        confirmChangeUsername = new JButton("Confirm new username");
        confirmChangeUsername.setForeground(Color.WHITE);
        confirmChangeUsername.setBackground(new Color(46, 139, 87));
        confirmChangeUsername.setFocusPainted(false);
        confirmChangeUsername.addActionListener(actionListener);

        usernamePanel.add(changeUsername);
        usernamePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        usernamePanel.add(changeUsernameField);
        usernamePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        usernamePanel.add(confirmChangeUsername);
        content.add(usernamePanel, BorderLayout.CENTER);

        passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Change Password"));
        passwordPanel.setBackground(Color.WHITE);

        confirmOldPassword = new JLabel("Enter old password");
        confirmOldPasswordField = new JPasswordField(20);
        confirmOldPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        changePassword = new JLabel("Enter new password");
        changePasswordField = new JPasswordField(20);
        changePasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        confirmChangePassword = new JButton("Confirm new password");
        confirmChangePassword.setForeground(Color.WHITE);
        confirmChangePassword.setBackground(new Color(46, 139, 87));
        confirmChangePassword.setFocusPainted(false);
        confirmChangePassword.addActionListener(actionListener);

        passwordPanel.add(confirmOldPassword);
        passwordPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        passwordPanel.add(confirmOldPasswordField);
        passwordPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        passwordPanel.add(changePassword);
        passwordPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        passwordPanel.add(changePasswordField);
        passwordPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        passwordPanel.add(confirmChangePassword);
        content.add(passwordPanel, BorderLayout.SOUTH);

        editProfileFrame.setVisible(true);
    }
}
