package Client;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.Scanner;

// Application client
public class Client {
    private static Socket socket;
    public static void main(String[] args) throws Exception {

        // Ask user for server address and port
        Scanner scanner = new Scanner(System.in);
        String serverAddress = getServerAddress(scanner);
        int serverPort = getServerPort(scanner);


// Création d'une nouvelle connexion aves le serveur
        socket = new Socket(serverAddress, serverPort);
        System.out.format("Server.Serveur lancé sur [%s:%d]", serverAddress, serverPort);
// Céatien d'un canal entrant pour recevoir les messages envoyés, par le serveur
        DataInputStream in = new DataInputStream(socket.getInputStream());
// Attente de la réception d'un message envoyé par le, server sur le canal
        String helloMessageFromServer = in.readUTF();
        System.out.println(helloMessageFromServer);
// fermeture de La connexion avec le serveur
        socket.close();
    }

    private static String getServerAddress(Scanner scanner) {
        System.out.println("Please enter a valid server IP address : ");
        String serverAddress = scanner.nextLine();

        while (!isServerAddressValid(serverAddress)) {
            System.out.println("Address is invalid, please enter a new address: ");
            serverAddress = scanner.nextLine();
        }
        return serverAddress;
    }

    private static int getServerPort(Scanner scanner) {
        System.out.println("Please enter server port (5000-5050) : ");
        String serverPort = scanner.nextLine();

        while (!isServerPortValid(serverPort)) {
            System.out.println("Server port is invalid, please enter a new port: ");
            serverPort = scanner.nextLine();
        }
        return Integer.parseInt(serverPort);
    }

    private static boolean isServerAddressValid(String serverAddress) {
        String[] splitServerAddress = serverAddress.split("\\.");
        if (splitServerAddress.length != 4) {
            return false;
        }

        for (String octet : splitServerAddress) {
            try {
                int parsedOctet = Integer.parseInt(octet);
                if (parsedOctet < 0 || parsedOctet > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

    private static boolean isServerPortValid(String serverPort) {
        try {
            int parsedServerPort = Integer.parseInt(serverPort);
            if (parsedServerPort < 5000 || parsedServerPort > 5050) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}