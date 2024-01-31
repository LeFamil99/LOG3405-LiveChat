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
            Listener.setReuseAddress(true);
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

    public void handleNewMessage() {
        //TODO: Cette méthode sera appelée par les ClientHandlers. Gérer le message reçu (l'enregistrer et lle transmettre aux autres clients)
    }

    public void close() throws Exception {
        Listener.close();
    }
}
