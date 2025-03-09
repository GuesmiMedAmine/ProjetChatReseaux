package client;

public class ClientApp {
    public static void main(String[] args) {
        // Adresse et port du serveur
        String host = "localhost";
        int port = 1234;

        // On lance le client
        Client client = new Client(host, port);
        client.start();
    }
}
