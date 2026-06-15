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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * MessagingFrame.java
 *
 * Where users messages each other
 *
 * @author Timothy Murray, L11
 *
 * @version December 8 2024
 */
public class MessagingFrame extends JComponent implements Runnable {
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    User user;
    String selectedUser;
    JFrame messagingFrame;
    Container content;
    JButton sendButton;
    JButton backButton;
    JList<String> messages;
    JTextField message;
    DefaultListModel<String> listModel;
    JButton deleteButton;
    String selectedMessage;

    public MessagingFrame(Socket socket, String selectedUser, User user, ObjectInputStream ois,
            ObjectOutputStream oos) {
        this.socket = socket;
        this.selectedUser = selectedUser;
        this.user = user;
        this.ois = ois;
        this.oos = oos;
        try {
            socket.setSoTimeout(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == backButton) {
                SwingUtilities.invokeLater(new ProfileFrame(socket, user, ois, oos));
                messagingFrame.dispose();
            }
            if (e.getSource() == sendButton) {
                try {
                    oos.writeObject(new Task("SendMessage"));
                    oos.flush();
                    oos.writeObject(new Task(message.getText()));
                    oos.flush();
                    oos.writeObject(new Task(selectedUser));
                    oos.flush();
                    oos.writeObject(user);
                    oos.flush();
                    message.setText("");
                    content.repaint();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (e.getSource() == deleteButton) {
                if (selectedMessage != null) {
                    try {
                        oos.writeObject(new Task("DeleteMessage"));
                        oos.flush();
                        oos.writeObject(new Task(selectedMessage.substring(4)));
                        oos.flush();
                        oos.writeObject(user);
                        oos.flush();
                        oos.writeObject(new Task(selectedUser));
                        oos.flush();
                        content.repaint();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    public void run() {
        try {
            messagingFrame = new JFrame("Messaging with " + selectedUser);
            content = messagingFrame.getContentPane();
            content.setLayout(new BorderLayout(10, 10));
            content.setBackground(new Color(207, 185, 145));

            messagingFrame.setSize(800, 600);
            messagingFrame.setLocationRelativeTo(null);
            messagingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel messagingPanel = new JPanel();
            messagingPanel.setLayout(new BoxLayout(messagingPanel, BoxLayout.Y_AXIS));
            messagingPanel.setBackground(new Color(207, 185, 145));
            messagingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JPanel messagesPanel = new JPanel(new BorderLayout());
            messagesPanel.setBackground(new Color(207, 185, 145));

            oos.writeObject(new Task("LoadMessages"));
            oos.flush();
            oos.writeObject(new Task(selectedUser));
            oos.flush();
            oos.writeObject(user);
            oos.flush();

            Task s = (Task) ois.readObject();
            int size = Integer.parseInt(s.getTask());
            if (size > 0) {
                listModel = new DefaultListModel<>();
                String[] msgs = new String[size];
                for (int i = 0; i < size; i++) {
                    Task t = (Task) ois.readObject();
                    msgs[i] = t.getTask();
                    listModel.addElement(msgs[i]);
                }
                messages = new JList<>(listModel);
                messages.addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            selectedMessage = messages.getSelectedValue();
                        }
                    }
                });
                JScrollPane scrollPane = new JScrollPane(messages);
                scrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 400));
                messagesPanel.add(scrollPane, BorderLayout.CENTER);
            }

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
            inputPanel.setBackground(new Color(207, 185, 145));

            message = new JTextField();
            message.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            sendButton = new JButton("Send");
            sendButton.setBackground(new Color(0, 0, 0));
            sendButton.setOpaque(true);
            sendButton.setForeground(new Color(196, 191, 192));
            sendButton.setMaximumSize(new Dimension(100, 40));
            sendButton.addActionListener(actionListener);

            deleteButton = new JButton("Delete");
            deleteButton.setBackground(new Color(0, 0, 0));
            deleteButton.setOpaque(true);
            deleteButton.setForeground(new Color(196, 191, 192));
            deleteButton.setMaximumSize(new Dimension(100, 40));
            deleteButton.addActionListener(actionListener);

            inputPanel.add(message);
            inputPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            inputPanel.add(sendButton);
            inputPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            inputPanel.add(deleteButton);

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

            messagingPanel.add(messagesPanel);
            messagingPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            messagingPanel.add(inputPanel);
            messagingPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            messagingPanel.add(backButtonPanel);

            content.add(messagingPanel, BorderLayout.CENTER);

            messagingFrame.setVisible(true);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Issue with messaging frame", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
