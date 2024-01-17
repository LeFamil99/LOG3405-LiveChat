package Server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;
public class Serveur {
    private static ServerSocket Listener; // Application Server.Serveur
    public static void main(String[] args) throws Exception {
        int clientNumber = 0;

        // Ask user for server address and port
        Scanner scanner = new Scanner(System.in);
        String serverAddress = getServerAddress(scanner);
        int serverPort = getServerPort(scanner);

        Listener = new ServerSocket();
        Listener.setReuseAddress(true);
        InetAddress serverIP = InetAddress.getByName(serverAddress);

        Listener.bind(new InetSocketAddress(serverIP, serverPort));
        System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);
        try {
            while (true) {
                // accept() function blocks until new user connects
                new ClientHandler(Listener.accept(), clientNumber++).start();
            }
        } finally {
            Listener.close();
        }
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
