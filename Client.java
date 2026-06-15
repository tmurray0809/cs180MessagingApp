import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client.java
 *
 * The client that connects to the server
 *
 * @author Timothy Murray, L11
 *
 * @version December 8 2024
 */

public class Client implements ClientInterface {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        Socket socket = null;

        try {
            socket = new Socket(HOST, PORT);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to connect to server", "Error", JOptionPane.ERROR_MESSAGE);
        }

        SwingUtilities.invokeLater(new LoginFrame(socket));

    }
}
