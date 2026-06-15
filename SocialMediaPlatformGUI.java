import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * SocialMediaPlatformGUI.java
 *
 * The main GUI for the social media platform
 *
 * @author Purdue CS: Shreyas Aryah
 * @version November 29, 2024
 */

public class SocialMediaPlatformGUI extends JFrame {

    private String currentUsername;
    private JTabbedPane mainTabbedPane;
    private JPanel loginPanel;
    private JPanel homePanel;
    private JPanel friendsPanel;
    private JPanel messagesPanel;
    private JPanel headerPanel;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;

    private JTextArea postTextArea;
    private JButton submitPostButton;
    private JTable postsTable;
    private DefaultTableModel postsTableModel;

    private JTextField searchUserField;
    private JButton searchUserButton;
    private JTable friendsTable;
    private DefaultTableModel friendsTableModel;

    private JTextField sendMessageField;
    private JButton sendMessageButton;
    private JTable messagesTable;
    private DefaultTableModel messagesTableModel;
    private JComboBox<String> friendsComboBox;

    public SocialMediaPlatformGUI() {
        setTitle("Social Media Platform");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        setupHeaderPanel();
        setupLoginPanel();
        setupMainPanels();

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(loginPanel, BorderLayout.CENTER);
    }

    private void initializeComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        createAccountButton = new JButton("Create Account");

        postTextArea = new JTextArea(5, 40);
        submitPostButton = new JButton("Post");

        String[] postColumns = { "User", "Post Content", "Timestamp" };
        postsTableModel = new DefaultTableModel(postColumns, 0);
        postsTable = new JTable(postsTableModel);

        searchUserField = new JTextField(20);
        searchUserButton = new JButton("Search");

        String[] friendColumns = { "Username", "Status" };
        friendsTableModel = new DefaultTableModel(friendColumns, 0);
        friendsTable = new JTable(friendsTableModel);

        sendMessageField = new JTextField(30);
        sendMessageButton = new JButton("Send");
        friendsComboBox = new JComboBox<>();

        String[] messageColumns = { "From", "To", "Message" };
        messagesTableModel = new DefaultTableModel(messageColumns, 0);
        messagesTable = new JTable(messagesTableModel);
    }

    private void setupHeaderPanel() {
        headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("Welcome to [APP NAME]");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);
    }

    private void setupLoginPanel() {
        loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(createAccountButton);

        loginButton.addActionListener(e -> performLogin());
        createAccountButton.addActionListener(e -> performCreateAccount());
    }

    private void setupMainPanels() {
        mainTabbedPane = new JTabbedPane();

        homePanel = new JPanel(new BorderLayout());
        JPanel postPanel = new JPanel(new BorderLayout());
        postPanel.add(new JScrollPane(postTextArea), BorderLayout.CENTER);
        postPanel.add(submitPostButton, BorderLayout.EAST);
        homePanel.add(postPanel, BorderLayout.NORTH);
        homePanel.add(new JScrollPane(postsTable), BorderLayout.CENTER);

        friendsPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.add(searchUserField);
        searchPanel.add(searchUserButton);
        friendsPanel.add(searchPanel, BorderLayout.NORTH);
        friendsPanel.add(new JScrollPane(friendsTable), BorderLayout.CENTER);

        messagesPanel = new JPanel(new BorderLayout());
        JPanel messageInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        messageInputPanel.add(friendsComboBox);
        messageInputPanel.add(sendMessageField);
        messageInputPanel.add(sendMessageButton);
        messagesPanel.add(messageInputPanel, BorderLayout.NORTH);
        messagesPanel.add(new JScrollPane(messagesTable), BorderLayout.CENTER);

        mainTabbedPane.addTab("Home", homePanel);
        mainTabbedPane.addTab("Friends", friendsPanel);
        mainTabbedPane.addTab("Messages", messagesPanel);
    }

    private void performLogin() {
        currentUsername = usernameField.getText();
        System.out.println("Logged in as: " + currentUsername);
        getContentPane().remove(loginPanel);
        getContentPane().add(mainTabbedPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void performCreateAccount() {
        System.out.println("Create account clicked");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SocialMediaPlatformGUI gui = new SocialMediaPlatformGUI();
            gui.setVisible(true);
        });
    }
}
