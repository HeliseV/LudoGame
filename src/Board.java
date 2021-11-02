import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.Math;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Board extends JFrame {
    private boolean gameHasStarted = false;
    private int playersPlayingCurrentGame = 0;
    private int totalNumberPlayers;
    private int PlayertoPlay = 0;
    BoardPositions GPath = new BoardPositions();
    JPanel BoardPanel = new JPanel();
    JPanel Background = new JPanel();
    JPanel diePanel = new JPanel();
    JLayeredPane gameBoardGUI = new JLayeredPane();
    JButton dieButton = new JButton("Roll Die");
    Player[] players = new Player[4];
    int myPlayerIndex;


    private int currentRollValue = 0;
    JPanel RollInfo = new JPanel(new GridBagLayout());
    JPanel eRollInfo = new JPanel(new GridBagLayout());

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your Username for the group chat: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Board LudoGameBoard = new Board(socket, username);
    }

    public Board(Socket socket, String username) {

        players[0] = new Player('R');
        players[1] = new Player('B');
        players[2] = new Player('G');
        players[3] = new Player('Y');

        //window configuration
        setTitle("Ludo Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setVisible(true);
        guiSetup();

        //set position and size of window
        setBackground(new Color(255, 255, 255));
        setSize(1000, 640);
        setLocationRelativeTo(null);

        //networking
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
        try {
            listenForMessage();
            sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel Panels(int x, int y, int width, int height, int r, int g, int b, float stroke) {
        JPanel panelTemplate = new JPanel(new GridLayout(1, 0));
        panelTemplate.setBounds(x, y, width, height);
        panelTemplate.setBackground(new Color(r, g, b));
        panelTemplate.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(stroke)));
        return panelTemplate;
    }

    private static JPanel homePath(int x, char C) {
        int xpos, ypos;
        switch (C) {
            case 'R' -> {
                ypos = BoardPositions.redLocation[x].gety();
                xpos = BoardPositions.redLocation[x].getx();
                return Panels(xpos, ypos, 40, 40, 255, 0, 0, 0.5f);
            }
            case 'B' -> {
                xpos = BoardPositions.blueLocation[x].getx();
                ypos = BoardPositions.blueLocation[x].gety();
                return Panels(xpos, ypos, 40, 40, 0, 0, 255, 0.5f);
            }
            case 'G' -> {
                ypos = BoardPositions.greenLocation[x].gety();
                xpos = BoardPositions.greenLocation[x].getx();
                return Panels(xpos, ypos, 40, 40, 0, 255, 0, 0.5f);
            }
            case 'Y' -> {
                ypos = BoardPositions.yellowLocation[x].gety();
                xpos = BoardPositions.yellowLocation[x].getx();
                return Panels(xpos, ypos, 40, 40, 255, 255, 0, 0.5f);
            }
        }
        return new JPanel();
    }

    public static class CenterPanel extends JPanel {
        CenterPanel() {
        }

        public void paint(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;

            g2D.setPaint(Color.RED);
            int[] xPoints = {0, 120, 60};
            int[] yPoints = {0, 0, 60};
            g2D.fillPolygon(xPoints, yPoints, 3);


            g2D.setPaint(Color.BLUE);
            int[] xPoints2 = {0, 0, 60};
            int[] yPoints2 = {0, 120, 60};
            g2D.fillPolygon(xPoints2, yPoints2, 3);

            g2D.setPaint(Color.YELLOW);
            int[] xPoints3 = {120, 0, 60};
            int[] yPoints3 = {120, 120, 60};
            g2D.fillPolygon(xPoints3, yPoints3, 3);

            g2D.setPaint(Color.GREEN);
            int[] xPoints4 = {120, 120, 60};
            int[] yPoints4 = {120, 0, 60};
            g2D.fillPolygon(xPoints4, yPoints4, 3);

        }


    }

    public int rollDie() {
        int rollValue = (int) ((Math.random() * 6) + 1);
        currentRollValue = rollValue;
        return rollValue;
    }

    private class theGambler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (PlayertoPlay == myPlayerIndex) {
                if (myPlayerIndex == 0 && !gameHasStarted) {
                    gameHasStarted = true;
                    if (totalNumberPlayers < 4) {
                        playersPlayingCurrentGame = totalNumberPlayers;
                    } else {
                        playersPlayingCurrentGame = 3;
                    }
                }

                currentRollValue = rollDie();
                if (currentRollValue != 6) {
                    if (PlayertoPlay < playersPlayingCurrentGame) {
                        PlayertoPlay++;
                    } else {
                        PlayertoPlay = 0;
                    }
                    diePanel.setVisible(false);
                }

                players[myPlayerIndex].movePiece(currentRollValue);
                showImage(currentRollValue);
                if (players[myPlayerIndex].hasWon()) {
                    determineWinner(true);
                    try {
                        bufferedWriter.write("YL");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    } catch (IOException ex) {
                        System.out.println("Error Action Event");
                    }
                } else {
                    try {
                        bufferedWriter.write(myPlayerIndex + "@" + currentRollValue + "@" + username + "@" + PlayertoPlay);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    } catch (IOException ex) {
                        System.out.println("Error Action Event");
                    }
                }
            }
        }
    }

    private void showImage(int num) {
        RollInfo.removeAll();
//        JLabel rollInfoLabel = new JLabel("You rolled a value of ");
        JLabel rollInfoLabel2 = new JLabel("You rolled a value of\n " + currentRollValue);
        rollInfoLabel2.setFont(new Font("Verdana", Font.BOLD, 20));

//        RollInfo.add(rollInfoLabel);
        RollInfo.add(rollInfoLabel2);

        BoardPanel.removeAll();
        JLabel DieImageLabel = new JLabel();
        ImageIcon DieImageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("dice_" + num + "w.png")));
        Image DieImage = DieImageIcon.getImage();
        Image resizedDieImage = DieImage.getScaledInstance(95, 95, Image.SCALE_SMOOTH);
        DieImageLabel.setIcon(new ImageIcon(resizedDieImage));
        BoardPanel.add(DieImageLabel);
        repaint();
        revalidate();
    }

    private void createBackground(String relativePath) {
        Background.removeAll();
        JLabel bg = new JLabel();
        ImageIcon backgroundIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(relativePath)));
        Image backgroundImage = backgroundIcon.getImage();
        Image backgroundImageScaled = backgroundImage.getScaledInstance(1000, 700, Image.SCALE_SMOOTH);
        bg.setIcon(new ImageIcon(backgroundImageScaled));
        Background.add(bg);
        repaint();
        revalidate();
    }

    public void sendMessage() {
        new Thread(() -> {
            try {
                bufferedWriter.write(username);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                Scanner scanner = new Scanner(System.in);
                while (socket.isConnected()) {
                    String messageToSend = scanner.nextLine();
                    bufferedWriter.write(username + ": " + messageToSend);
                    System.out.println(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }).start();

    }

    public void listenForMessage() {
        new Thread(() -> {
            String messageFromGroupChat;

            while (socket.isConnected()) {
                try {
                    messageFromGroupChat = bufferedReader.readLine();
                    System.out.println(messageFromGroupChat);
                    if (messageFromGroupChat.length() == 3) {
                        String[] message = messageFromGroupChat.split("@", 2);
                        myPlayerIndex = Integer.parseInt(message[1]);
                        if (myPlayerIndex < 4) {
                            createBackground(players[Integer.parseInt(message[1])].colour + "bg.png");
                        } else {
//                                Background
                            diePanel.removeAll();
                        }
                    } else if (messageFromGroupChat.length() == 1) {
                        totalNumberPlayers = Integer.parseInt(messageFromGroupChat);
                    } else if (messageFromGroupChat.length() == 2) {
                        if (messageFromGroupChat.equals("00")) {
                            JLabel rollInfoLabel3 = new JLabel(" Game Terminated.");
                            RollInfo.removeAll();
                            eRollInfo.removeAll();
                            RollInfo.add(rollInfoLabel3);
                            eRollInfo.add(rollInfoLabel3);
                        } else if (messageFromGroupChat.equals("YL")) {
                            determineWinner(players[myPlayerIndex].hasWon());
                        }
                        diePanel.setVisible(false);
                        repaint();
                        revalidate();
                    } else {
                        if (!gameHasStarted) {
                            gameHasStarted = true;
                            if (totalNumberPlayers < 4) {
                                playersPlayingCurrentGame = totalNumberPlayers;
                            } else {
                                playersPlayingCurrentGame = 3;
                            }
                        }
                        String[] message = messageFromGroupChat.split("@", 4);
                        players[Integer.parseInt(message[0])].movePiece(Integer.parseInt(message[1]));
                        eRollInfo.removeAll();
                        PlayertoPlay = Integer.parseInt(message[3]);
                        Color color;
                        try {
                            Field field = Class.forName("java.awt.Color").
                                    getField(players[Integer.parseInt(message[0])].colour.toLowerCase());
                            color = (Color) field.get(null);
                        } catch (Exception e) {
                            color = null; // Not defined
                        }
                        eRollInfo.setBackground(color);
                        JLabel rollInfoLabel2 = new JLabel(message[2] + " rolled a value of " + message[1]);
                        rollInfoLabel2.setFont(new Font("Verdana", Font.BOLD, 15));
                        rollInfoLabel2.setForeground(Color.white);
                        eRollInfo.add(rollInfoLabel2);
                        repaint();
                        revalidate();
                        if (PlayertoPlay == myPlayerIndex) {
                            JLabel rollInfoLabel = new JLabel("YOUR TURN");
                            rollInfoLabel.setFont(new Font("Verdana", Font.BOLD, 25));
                            Color color2;
                            diePanel.setVisible(true);
                            try {
                                Field field = Class.forName("java.awt.Color").
                                        getField(players[myPlayerIndex].colour.toLowerCase());
                                color2 = (Color) field.get(null);
                            } catch (Exception e) {
                                color2 = null; // Not defined
                            }
                            rollInfoLabel.setForeground(color2);
                            RollInfo.removeAll();
                            RollInfo.add(rollInfoLabel);
                            repaint();
                            revalidate();
                        }

                    }

                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    public void determineWinner(boolean winner) {
        JPanel gameOverPanel = new JPanel(new GridBagLayout());
        gameOverPanel.setBounds(0, 0, 1000, 700);
        JLabel gameOver = new JLabel();
        if (Objects.equals(winner, false)) {
            gameOverPanel.setBackground(Color.BLACK);
            gameOver.setText("You Lost!");
            gameOver.setForeground(Color.white);
        } else {
            gameOver.setText("You Won!");
            gameOverPanel.setBackground(Color.white);
            gameOver.setForeground(Color.black);
        }
        gameOver.setFont(new Font("Verdana", Font.BOLD, 100));
        gameOverPanel.add(gameOver);
        gameBoardGUI.add(gameOverPanel, Integer.valueOf(50));
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guiSetup() {
        Background.setBounds(0, 0, 1000, 700);
        Background.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        createBackground("Defaultbg.png");
        //JLayeredPane to allow for other objects to overlay board
        gameBoardGUI.setBounds(0, 0, 1000, 700);
        gameBoardGUI.setBackground(new Color(255, 255, 255));
        add(gameBoardGUI);

        //Board
        diePanel.setBounds(600, 550, 100, 50);
        diePanel.add(this.dieButton);
        diePanel.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        dieButton.addActionListener(new theGambler());

        //adding piece Image to GUI
        for (int x = 0; x < 4; x++) {
            gameBoardGUI.add(players[0].pieces[x], Integer.valueOf(2));
            gameBoardGUI.add(players[1].pieces[x], Integer.valueOf(2));
            gameBoardGUI.add(players[2].pieces[x], Integer.valueOf(2));
            gameBoardGUI.add(players[3].pieces[x], Integer.valueOf(2));
        }

        gameBoardGUI.add(Background, Integer.valueOf(-1));
        gameBoardGUI.add(diePanel, Integer.valueOf(1));
        CenterPanel centerPanel = new CenterPanel();
        centerPanel.setBounds(BoardPositions.mainLocation[0].getx() + 200, BoardPositions.mainLocation[0].gety(), 120, 120);
        gameBoardGUI.add(centerPanel, Integer.valueOf(1));

        JPanel[] whiteOverlay = new JPanel[4];

        JPanel BluePanel = Panels(BoardPositions.mainLocation[0].getx() - 40, BoardPositions.mainLocation[0].gety() - 240,
                240, 240, 0, 0, 255, 5.0f);
        gameBoardGUI.add(BluePanel, Integer.valueOf(0));
        whiteOverlay[0] = Panels(BoardPositions.mainLocation[0].getx(), BoardPositions.mainLocation[0].gety() - 200,
                160, 160, 255, 255, 255, 2.0f);
        gameBoardGUI.add(whiteOverlay[0], Integer.valueOf(1));

        JPanel RedPanel = Panels(BoardPositions.mainLocation[0].getx() + 320, BoardPositions.mainLocation[0].gety() - 240,
                240, 240, 255, 0, 0, 5.0f);
        gameBoardGUI.add(RedPanel, Integer.valueOf(0));
        whiteOverlay[1] = Panels(BoardPositions.mainLocation[0].getx(), BoardPositions.mainLocation[0].gety() + 160,
                160, 160, 255, 255, 255, 2.0f);
        gameBoardGUI.add(whiteOverlay[1], Integer.valueOf(1));

        JPanel GreenPanel = Panels(BoardPositions.mainLocation[0].getx() + 320, BoardPositions.mainLocation[0].gety() + 120,
                240, 240, 0, 255, 0, 5.0f);
        gameBoardGUI.add(GreenPanel, Integer.valueOf(0));
        whiteOverlay[2] = Panels(BoardPositions.mainLocation[0].getx() + 360, BoardPositions.mainLocation[0].gety() + 160,
                160, 160, 255, 255, 255, 2.0f);
        gameBoardGUI.add(whiteOverlay[2], Integer.valueOf(1));

        JPanel YellowPanel = Panels(BoardPositions.mainLocation[0].getx() - 40, BoardPositions.mainLocation[0].gety() + 120,
                240, 240, 255, 255, 0, 5.0f);
        gameBoardGUI.add(YellowPanel, Integer.valueOf(0));
        whiteOverlay[3] = Panels(BoardPositions.mainLocation[0].getx() + 360, BoardPositions.mainLocation[0].gety() - 200,
                160, 160, 255, 255, 255, 2.0f);
        gameBoardGUI.add(whiteOverlay[3], Integer.valueOf(1));

        JPanel greentile = Panels(BoardPositions.greenStart.getx(), BoardPositions.greenStart.gety()
                , 40, 40, 0, 255, 0, 0.7f);
        gameBoardGUI.add(greentile, Integer.valueOf(1));
        JPanel redtile = Panels(BoardPositions.redStart.getx(), BoardPositions.redStart.gety()
                , 40, 40, 255, 0, 0, 0.7f);
        gameBoardGUI.add(redtile, Integer.valueOf(1));
        JPanel bluetile = Panels(BoardPositions.blueStart.getx(), BoardPositions.blueStart.gety()
                , 40, 40, 0, 0, 255, 0.7f);
        gameBoardGUI.add(bluetile, Integer.valueOf(1));
        JPanel yellowtile = Panels(BoardPositions.yellowStart.getx(), BoardPositions.yellowStart.gety()
                , 40, 40, 255, 255, 0, 0.7f);
        gameBoardGUI.add(yellowtile, Integer.valueOf(1));

        gameBoardGUI.add(RollInfo, Integer.valueOf(1));
        RollInfo.setBounds(620, 30, 340, 100);
        gameBoardGUI.add(eRollInfo, Integer.valueOf(1));
        eRollInfo.setBounds(620, 300, 340, 100);

        gameBoardGUI.add(BoardPanel, Integer.valueOf(0));
        BoardPanel.setBounds(600, 150, 400, 100);
        BoardPanel.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));

        for (int x = 0; x < 52; x++) {
            int xpos = BoardPositions.mainLocation[x].getx();
            int ypos = BoardPositions.mainLocation[x].gety();
            JPanel pathPanel = Panels(xpos, ypos, 40, 40, 255, 255, 255, 0.7f);
            gameBoardGUI.add(pathPanel);
        }

        for (int x = 0; x < 5; x++) {
            JPanel rPanel = homePath(x, 'R');
            gameBoardGUI.add(rPanel);
            JPanel bPanel = homePath(x, 'B');
            gameBoardGUI.add(bPanel);
            JPanel gPanel = homePath(x, 'G');
            gameBoardGUI.add(gPanel);
            JPanel yPanel = homePath(x, 'Y');
            gameBoardGUI.add(yPanel);
        }
    }
}