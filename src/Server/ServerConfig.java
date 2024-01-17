package Server;

import java.util.Scanner;

public class ServerConfig {
    private String serverAddress;
    private int serverPort;

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public ServerConfig() {
        this.serverAddress = initializeServerAddress();
        this.serverPort = initializeServerPort();
    }

    private String initializeServerAddress() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a valid server IP address : ");
        String serverAddress = scanner.nextLine();

        while (!isServerAddressValid(serverAddress)) {
            System.out.println("Address is invalid, please enter a new address: ");
            serverAddress = scanner.nextLine();
        }
        return serverAddress;
    }

    private int initializeServerPort() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter server port (5000-5050) : ");
        String serverPort = scanner.nextLine();

        while (!isServerPortValid(serverPort)) {
            System.out.println("Server port is invalid, please enter a new port: ");
            serverPort = scanner.nextLine();
        }
        return Integer.parseInt(serverPort);
    }

    private boolean isServerAddressValid(String serverAddress) {
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

    private boolean isServerPortValid(String serverPort) {
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
