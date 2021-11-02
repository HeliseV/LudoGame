import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    JFrame frame = new JFrame();
    JButton loginButton = new JButton("Login");
    JButton signupButton = new JButton("Signup");
    JTextField userIDField = new JTextField();
    JPasswordField userPasswordField = new JPasswordField();
    JLabel userIDLabel = new JLabel("userID:");
    JLabel userPasswordLabel = new JLabel("password:");
    JLabel messageLabel = new JLabel();

    private ServerSocket serverSocket;
    private static int numberOfSockets = 0;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

    }

    public void startServer() {

        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected." + getNumberOfSockets());
                PlayerHandler playerHandler = new PlayerHandler(socket, numberOfSockets);
                numberOfSockets++;

                Thread thread = new Thread(playerHandler);
                thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }

    public static int getNumberOfSockets() {
        return numberOfSockets;
    }
}

