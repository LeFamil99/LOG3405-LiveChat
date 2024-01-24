package Client;

import java.io.DataInputStream;
import java.io.EOFException;
import java.net.Socket;

public class Client {
    private static Socket socket;
    public Client(ServerConfig config) throws Exception {
        socket = new Socket(config.getServerAddress(), config.getServerPort());
        System.out.format("Server.Serveur lancé sur [%s:%d]", config.getServerAddress(), config.getServerPort());

    }

    public void listen() throws Exception {
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

    public void close() throws Exception {
        socket.close();
    }
}
