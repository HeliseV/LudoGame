import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class OpeningPage {
    JFrame frame = new JFrame();
    JButton loginButton = new JButton();

    public OpeningPage() {
        frame.setTitle("Ludo Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
        loginButton.setBounds(125, 200, 100, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(new theGambler());
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
    private void  tryo (){
        frame.dispose();
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your Username for the group chat: ");
            String username = scanner.nextLine();
            Socket socket = new Socket("localhost", 1234);
            Board LudoGameBoard = new Board(socket,username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
