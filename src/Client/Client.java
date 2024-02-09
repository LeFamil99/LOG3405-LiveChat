package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    State state;
    
    public Client(ServerConfig config) throws Exception {
        socket = new Socket(config.getServerAddress(), config.getServerPort());
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
                if(message.length() <= 200) {
                    out.writeUTF(message);
                } else {
                    System.out.println("Le message est trop long, veuillez entrer un message de moins de 200 caractères.");
                }
            } catch (EOFException e) {
                System.out.println("Connection fermées par le serveur");
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
            String answer = in.readUTF();
            if(answer.equals("accepted")){
                System.out.println("Connecté avec succès");
                this.state = State.CONNECTED;
            } else {
                System.out.println("Mot de passe invalide");
            }
        }
        DataInputStream in = new DataInputStream(socket.getInputStream());
        String lastMessages = in.readUTF();
        System.out.println(lastMessages);
    }
}
