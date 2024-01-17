package Server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Server {
    private ServerSocket Listener;
    public Server(ServerConfig config) throws Exception {
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
            new ClientHandler(Listener.accept(), 0).start();
        }
    }

    public void close() throws Exception {
        Listener.close();
    }
}
