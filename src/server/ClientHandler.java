package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientHandler implements Runnable {
    private DatagramPacket packetRecu;
    private Server server;

    public ClientHandler(Server server, DatagramPacket packetRecu) {
        this.server = server;
        this.packetRecu = packetRecu;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(); // Nouveau socket pour réponse
            InetAddress clientAddress = packetRecu.getAddress();
            int clientPort = packetRecu.getPort();
            String messageRecu = new String(packetRecu.getData(), 0, packetRecu.getLength());

            // Afficher l'adresse et le port du client qui a envoyé le message
            System.out.println("[Serveur] Message reçu de : " + clientAddress + " - Port : " + clientPort);

            // Si le message contient "Nom : ", c'est un nouvel utilisateur
            if (messageRecu.startsWith("Nom : ")) {
                String userName = messageRecu.substring(6);
                server.addClient(clientAddress, clientPort, userName);
                System.out.println("[Serveur] Nouveau client : " + userName);
            } else {
                // Identifier l'expéditeur
                String senderName = "Inconnu";
                for (int i = 0; i < server.getClientAddresses().size(); i++) {
                    if (server.getClientAddresses().get(i).equals(clientAddress) &&
                        server.getClientPorts().get(i) == clientPort) {
                        senderName = server.getClientNames().get(i);
                        break;
                    }
                }

                // Diffuser le message à tous les clients
                String messageDiffusion = senderName + " : " + messageRecu;
                byte[] buffer = messageDiffusion.getBytes();

                for (int i = 0; i < server.getClientAddresses().size(); i++) {
                    InetAddress destAddress = server.getClientAddresses().get(i);
                    int destPort = server.getClientPorts().get(i);

                    DatagramPacket packetEnvoi = new DatagramPacket(buffer, buffer.length, destAddress, destPort);
                    socket.send(packetEnvoi);
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
