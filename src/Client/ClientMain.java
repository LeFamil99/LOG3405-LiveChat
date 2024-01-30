package Client;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        Client client = new Client(new ServerConfig());

        client.close();
    }
}