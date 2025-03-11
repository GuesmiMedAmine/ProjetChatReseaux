package client;

public class ClientApp {
    public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = 1234;
        Client client = new Client(serverHost, serverPort);
        client.start();
    }
}
