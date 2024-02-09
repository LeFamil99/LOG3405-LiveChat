package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class ClientHandler extends Thread {
    private DatabaseService databaseService;
    private final Server server;
    DataOutputStream out;
    DataInputStream in;
    private String username;
    private String ip;
    private int port;
    private boolean isConnected;
    public ClientHandler(Socket socket, Server server, DatabaseService databaseService) throws IOException {
        this.databaseService = databaseService;
        this.server = server;

        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        ip = socket.getInetAddress().getHostAddress();
        port = socket.getPort();

        System.out.println("New connection with client at" + socket);
    }

    public void sendMessageToClient(Message message) throws Exception {
        out.writeUTF("[" + message.getUsername() + " - " +
                message.getIpAddress() + ":"
                + message.getPort() + " - " +
                Message.dateFormatter.format(message.getDate()) + "@"
                + Message.timeFormatter.format(message.getTime()) + "]: "
                + message.getMessage()
        );
    }

    public void run() {
        try {
            handleLogin();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleLogin() throws Exception {
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
            String message = in.readUTF();

            Message newMessage = new Message(message, username, ip, port, LocalDate.now(), LocalTime.now());
            server.handleNewMessage(newMessage);
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