package Client;

import java.io.DataInputStream;
import java.io.EOFException;
import java.net.Socket;

public class Client {
    private static Socket socket;
    // private ServerListener (thread)
    public Client(ServerConfig config) throws Exception {
        socket = new Socket(config.getServerAddress(), config.getServerPort());
        System.out.format("Server.Serveur lancé sur [%s:%d]", config.getServerAddress(), config.getServerPort());

        Thread listenToServerThread = new Thread(() -> {
            try {
                this.listenToServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        listenToServerThread.start();

        Thread listenToUserThread = new Thread(() -> {
            try {
                this.listenToServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        listenToUserThread.start();
    }

    public void listenToServer() throws Exception {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        while (true) {
            try {
                String helloMessageFromServer = in.readUTF();
                System.out.println(helloMessageFromServer);
            } catch (EOFException e) {
                System.out.println("Connection closed by the server.");
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listenToUser () {
        // TODO
    }

    public void close() throws Exception {
        socket.close();
    }
}
