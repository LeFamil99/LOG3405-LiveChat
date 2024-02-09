package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    // private ServerListener (thread)
    State state;
    
    public Client(ServerConfig config) throws Exception {
        socket = new Socket(config.getServerAddress(), config.getServerPort());
        System.out.format("Server.Serveur lancé sur [%s:%d]", config.getServerAddress(), config.getServerPort());
        this.state = State.NOT_CONNECTED;
        this.stateHandle();
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
                this.listenToUser();
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

    public void listenToUser() throws IOException {
        Scanner scanner = new Scanner(System.in);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        while (true){
            try {
                String message = scanner.nextLine();
                out.writeUTF(message);
            } catch (EOFException e) {
                System.out.println("Connection closed by the server.");
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void stateHandle() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while(this.state == State.NOT_CONNECTED){
            System.out.println("Enter Username");
            String username = scanner.nextLine();
            System.out.println("Enter Password");
            String password = scanner.nextLine();
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(username +"\n"+password);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String awnser = in.readUTF();
            if(awnser == "accepted"){
                System.out.println("Succes Conenction");
                this.state = State.CONNECTED;
            } else {
                System.out.println("Erreur dans la saisie du mot de passe");
            }
        }
        DataInputStream in = new DataInputStream(socket.getInputStream());
        String fifteenmessage = in.readUTF();
        System.out.println(fifteenmessage);
    }

    public void close() throws Exception {
        socket.close();
    }
}
