package Server;
public class ServerMain {
    public static void main(String[] args) throws Exception {
        DatabaseService databaseService = new DatabaseService();
        Server server = new Server(new ServerConfig(), databaseService);
        try {
            server.listen();
        } finally {
            server.close();
        }
    }
}
