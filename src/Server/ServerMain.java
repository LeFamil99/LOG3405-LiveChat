package Server;
public class ServerMain {
    public static void main(String[] args) throws Exception {
        Server server = new Server(new ServerConfig());
        try {
            server.listen();
        } finally {
            server.close();
        }
    }
}
