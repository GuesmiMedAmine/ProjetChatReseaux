package client;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    private String serverHost;
    private int serverPort;
    private DatagramSocket socket;
    private InetAddress serverAddress;

    public Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(serverHost);

            // Thread pour recevoir les messages du serveur
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
                byte[] buffer = message.getBytes();
                DatagramPacket packetEnvoi = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
                socket.send(packetEnvoi);

                if (message.equalsIgnoreCase("quit")) {
                    System.out.println("[Client] Déconnexion.");
                    break;
                }
            }

            // Fermeture
            socket.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
