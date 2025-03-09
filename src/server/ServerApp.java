package server;

public class ServerApp {
    public static void main(String[] args) {
        // Choisissez un port, par exemple 1234
        int port = 1234;

        // On instancie le serveur et on le démarre
        Server server = new Server(port);
        server.start();
    }
}
