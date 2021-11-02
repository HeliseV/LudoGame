import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class PlayerHandler implements Runnable{

    public static ArrayList<PlayerHandler> playerHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    int playerIndex;

    public PlayerHandler(Socket socket, int position) {
        try {
            this.playerIndex = position;
            this.socket = socket;
            this. bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            playerHandlers.add(this);
            clientMessage("I@"+ playerIndex);
            broadcastMessage(""+(playerIndex));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (PlayerHandler playerHandler : playerHandlers) {
            try {
                if (!playerHandler.clientUsername.equals(clientUsername)) {
                    playerHandler.bufferedWriter.write(messageToSend);
                    playerHandler.bufferedWriter.newLine();
                    playerHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void clientMessage(String messageToSend) {
        for (PlayerHandler playerHandler : playerHandlers) {
            try {
                if (playerHandler.clientUsername.equals(clientUsername)) {
                    playerHandler.bufferedWriter.write(messageToSend);
                    playerHandler.bufferedWriter.newLine();
                    playerHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        playerHandlers.remove(this);
        broadcastMessage("00");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
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
}
