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
 * LoginFrame.java
 *
 * The login frame for the GUI
 *
 * @author Timothy Murray, L11
 *
 * @version December 8 2024
 */
public class LoginFrame extends JComponent implements Runnable {

    JFrame loginFrame;
    JLabel usernameLabel;
    JLabel passwordLabel;
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;
    JButton registerButton;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public LoginFrame(Socket socket) {
        this.socket = socket;
    }

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginButton) {
                String username = usernameField.getText();
                char[] tempPassword = passwordField.getPassword();
                StringBuilder password = new StringBuilder();
                password.append(tempPassword);
                try {
                    User user = new User(username, password.toString());
                    Task task = new Task("Login");
                    oos.writeObject(task);
                    oos.flush();
                    oos.writeObject(user);
                    oos.flush();
                    try {
                        Task result = (Task) ois.readObject();
                        if (result.getTask().equals("Success")) {
                            User newUser = (User) ois.readObject();
                            JOptionPane.showMessageDialog(null,
                                    "Login Successful!", "Login Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                            SwingUtilities.invokeLater(new HomeFrame(socket, newUser, ois, oos));
                            loginFrame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Login Failed!", "Login Failed",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (e.getSource() == registerButton) {
                String username = usernameField.getText();
                char[] tempPassword = passwordField.getPassword();
                StringBuilder password = new StringBuilder();
                password.append(tempPassword);
                User user = new User(username, password.toString());
                try {
                    oos.writeObject(new Task("Register"));
                    oos.flush();
                    oos.writeObject(user);
                    oos.flush();
                    Task result = (Task) ois.readObject();
                    if (result.getTask().equals("Success")) {
                        User u = (User) ois.readObject();
                        JOptionPane.showMessageDialog(null,
                                "Successfully created new account!", "Register Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        SwingUtilities.invokeLater(new HomeFrame(socket, u, ois, oos));
                        loginFrame.dispose();
                    } else if (result.getTask().equals("Failure") || result.getTask().equals("Username taken")) {
                        JOptionPane.showMessageDialog(null,
                                "Registration failed! Username already taken!", "Register Failed",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    };

    @Override
    public void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            loginFrame = new JFrame("Loginnn");
            Container content = loginFrame.getContentPane();
            content.setLayout(new BorderLayout(10, 10));
            content.setBackground(new Color(207, 185, 145));

            loginFrame.setSize(800, 600);
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
            loginPanel.setBackground(new Color(207, 185, 145));
            loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JPanel usernamePanel = new JPanel();
            usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.Y_AXIS));
            usernamePanel.setBackground(new Color(207, 185, 145));

            usernameLabel = new JLabel("Username");
            usernameLabel.setAlignmentX(CENTER_ALIGNMENT);
            usernameField = new JTextField(20);
            usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            usernameField.setAlignmentX(CENTER_ALIGNMENT);

            usernamePanel.add(usernameLabel);
            usernamePanel.add(Box.createRigidArea(new Dimension(0, 10)));
            usernamePanel.add(usernameField);

            JPanel passwordPanel = new JPanel();
            passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
            passwordPanel.setBackground(new Color(207, 185, 145));

            passwordLabel = new JLabel("Password");
            passwordLabel.setAlignmentX(CENTER_ALIGNMENT);
            passwordField = new JPasswordField(20);
            passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            passwordField.setAlignmentX(CENTER_ALIGNMENT);

            passwordPanel.add(passwordLabel);
            passwordPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            passwordPanel.add(passwordField);

            loginPanel.add(usernamePanel);
            loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            loginPanel.add(passwordPanel);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.setBackground(new Color(207, 185, 145));

            loginButton = new JButton("Login");
            loginButton.setBackground(new Color(0, 0, 0));
            loginButton.setOpaque(true);
            loginButton.setForeground(new Color(196, 191, 192));
            loginButton.setMaximumSize(new Dimension(150, 40));
            loginButton.addActionListener(actionListener);

            registerButton = new JButton("Register");
            registerButton.setBackground(new Color(0, 0, 0));
            registerButton.setOpaque(true);
            registerButton.setForeground(new Color(196, 191, 192));
            registerButton.setMaximumSize(new Dimension(150, 40));
            registerButton.addActionListener(actionListener);

            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(loginButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
            buttonPanel.add(registerButton);
            buttonPanel.add(Box.createHorizontalGlue());

            content.add(loginPanel, BorderLayout.CENTER);
            content.add(buttonPanel, BorderLayout.SOUTH);

            loginFrame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Issue with login frame", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
