import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

/**
 * EditProfileFrame.jav
 * a
 *
 * Frame that allows you to edit your profile
 *
 * @author Timothy Murray, L11
 *
 * @version December 8 2024
 */
public class EditProfileFrame extends JComponent implements Runnable {
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

    public EditProfileFrame(Socket socket, User user, ObjectInputStream ois, ObjectOutputStream oos) {
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

        editProfileFrame = new JFrame("Edit Profile");
        Container content = editProfileFrame.getContentPane();
        content.setLayout(new BorderLayout());
        content.setBackground(new Color(207, 185, 145));

        editProfileFrame.setSize(800, 600);
        editProfileFrame.setLocationRelativeTo(null);
        editProfileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backButton = new JButton("Back");
        backButton.setBackground(new Color(0, 0, 0));
        backButton.setOpaque(true);
        backButton.setForeground(new Color(196, 191, 192));
        backButton.addActionListener(actionListener);
        content.add(backButton, BorderLayout.NORTH);

        usernamePanel = new JPanel();
        usernamePanel.setBackground(new Color(207, 185, 145));
        changeUsername = new JLabel("Enter new username");
        changeUsernameField = new JTextField(20);
        confirmChangeUsername = new JButton("Confirm new username");
        confirmChangeUsername.setBackground(new Color(0, 0, 0));
        confirmChangeUsername.setOpaque(true);
        confirmChangeUsername.setForeground(new Color(196, 191, 192));
        confirmChangeUsername.addActionListener(actionListener);
        usernamePanel.add(changeUsername);
        usernamePanel.add(changeUsernameField);
        usernamePanel.add(confirmChangeUsername);
        content.add(usernamePanel, BorderLayout.CENTER);

        passwordPanel = new JPanel();
        passwordPanel.setBackground(new Color(207, 185, 145));
        confirmOldPassword = new JLabel("Confirm new password");
        confirmOldPasswordField = new JPasswordField(20);
        changePassword = new JLabel("Confirm new password");
        changePasswordField = new JPasswordField(20);
        confirmChangePassword = new JButton("Confirm new password");
        confirmChangePassword.setBackground(new Color(0, 0, 0));
        confirmChangePassword.setOpaque(true);
        confirmChangePassword.setForeground(new Color(196, 191, 192));
        confirmChangePassword.addActionListener(actionListener);
        passwordPanel.add(confirmOldPassword);
        passwordPanel.add(confirmOldPasswordField);
        passwordPanel.add(changePassword);
        passwordPanel.add(changePasswordField);
        passwordPanel.add(confirmChangePassword);
        content.add(passwordPanel, BorderLayout.SOUTH);

        editProfileFrame.setVisible(true);
    }
}
