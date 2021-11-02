import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class OpeningPage {
    JFrame frame = new JFrame();
    JButton loginButton = new JButton("Login");
    JButton signupButton = new JButton("Signup");
    JTextField userIDField = new JTextField();
    JPasswordField userPasswordField = new JPasswordField();
    JLabel userIDLabel = new JLabel("userID:");
    JLabel userPasswordLabel = new JLabel("password:");
    JLabel messageLabel = new JLabel();

    public OpeningPage() {
        frame.setTitle("Ludo Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
        userIDLabel.setBounds(50, 100, 75, 25);
        userPasswordLabel.setBounds(50, 150, 75, 25);

        messageLabel.setBounds(125, 250, 250, 35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 25));

        userIDField.setBounds(125, 100, 200, 25);
        userPasswordField.setBounds(125, 150, 200, 25);

        loginButton.setBounds(125, 200, 100, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(new theGambler());

        signupButton.setBounds(225, 200, 100, 25);
        signupButton.setFocusable(false);
        signupButton.addActionListener(new theGambler());

        frame.add(userIDLabel);
        frame.add(userPasswordLabel);
        frame.add(messageLabel);
        frame.add(userIDField);
        frame.add(userPasswordField);
        frame.add(loginButton);
        frame.add(signupButton);
        frame.add(loginButton);
        frame.setBackground(new Color(255, 255, 255));
        frame.setSize(1000, 640);
        frame.setLocationRelativeTo(null);
    }

    private class theGambler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginButton) {
                tryo();
            }
        }
    }

    private void tryo() {
        frame.dispose();
        try {
            String username = userIDField.getText();
            Socket socket = new Socket("localhost", 1234);
            Board LudoGameBoard = new Board(socket, username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
