package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class ClientHandler extends Thread {
    private DatabaseService databaseService;
    private final Server server;
    DataOutputStream out;
    DataInputStream in;
    private String username;
    private boolean isConnected;
    public ClientHandler(Socket socket, Server server, DatabaseService databaseService) throws IOException {
        this.databaseService = databaseService;
        this.server = server;

        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        System.out.println("New connection with client at" + socket);
    }

    public void sendMessageToClient(Message message) {
        //TODO: Envoyer le message au client
    }

    public void run() {
        try {
            handleLogin();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleLogin() throws IOException {
        while (!isConnected) {
            String credentials = in.readUTF();
            String[] splitCredentials = credentials.split("\n");
            String username = splitCredentials[0];
            String password = splitCredentials[1];

            if (password.equals(databaseService.getUserPassword(username))) {
                isConnected = true;
                out.writeUTF("accepted");
            } else {
                out.writeUTF("refused");
            }
        }
        out.writeUTF(
                databaseService.getLastMessages(15)
                .stream()
                .map(this::formatMessage)
                .collect(Collectors.joining("\n"))
        );

        while (true) {
            //TODO: Toujours être à l'écoute du server et informer le server
        }
    }

    private String formatMessage(Message message) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return "[" +
                message.getUsername() +
                " - " +
                message.getIpAddress() +
                ":" +
                dateFormatter.format(message.getDate()) +
                "@" +
                timeFormatter.format(message.getTime()) +
                "]: " +
                message.getMessage();
    }
}