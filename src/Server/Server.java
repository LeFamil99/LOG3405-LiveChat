package Server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private DatabaseService databaseService;
    private ServerSocket Listener;
    private int numClients = 0;
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    public Server(ServerConfig config, DatabaseService databaseService) throws Exception {
        try {
            Listener = new ServerSocket();
            InetAddress serverIP = InetAddress.getByName(config.getServerAddress());

            Listener.bind(new InetSocketAddress(serverIP, config.getServerPort()));
            System.out.format("The server is running on %s:%d%n", config.getServerAddress(), config.getServerPort());
        } catch (Exception e) {
            throw new Exception("Error while initiating server: " + e.getMessage());
        }
    }

    public void listen() throws Exception {
        while (true) {
            Socket socket = Listener.accept();
            numClients++;
            this.clients.add(new ClientHandler(socket, this, databaseService));
        }
    }

    public void handleNewMessage(Message message) throws Exception {
        databaseService.addMessage(message);
        for (ClientHandler client : this.clients) {
            client.sendMessageToClient(message);
        }
    }

    public void close() throws Exception {
        Listener.close();
    }
}
