package client;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    private String serverHost;
    private int serverPort;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private String userName;

    public Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(serverHost);

            // Demander et envoyer le nom d'utilisateur
            System.out.print("Entrez votre nom : ");
            userName = scanner.nextLine();
            sendMessage("Nom : " + userName);

            // Thread pour recevoir les messages
            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        byte[] buffer = new byte[1024];
                        DatagramPacket packetRecu = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packetRecu);

                        String messageRecu = new String(packetRecu.getData(), 0, packetRecu.getLength());
                        System.out.println(messageRecu);
                    }
                } catch (IOException e) {
                    System.out.println("[Client] Connexion fermée.");
                }
            });
            receiveThread.start();

            // Envoi des messages
            while (true) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("quit")) {
                    System.out.println("[Client] Déconnexion.");
                    break;
                }
                sendMessage(message);
            }

            // Fermeture
            socket.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packetEnvoi = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
        socket.send(packetEnvoi);
    }
}