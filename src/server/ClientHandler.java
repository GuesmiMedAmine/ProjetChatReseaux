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
            DatagramSocket socket = new DatagramSocket(); // Socket dédié pour la réponse
            InetAddress clientAddress = packetRecu.getAddress();
            int clientPort = packetRecu.getPort();
            String messageRecu = new String(packetRecu.getData(), 0, packetRecu.getLength());

            System.out.println("[Serveur] Message reçu de : " + clientAddress + " - Port : " + clientPort);

            // Enregistrement du client s'il s'agit d'un nouveau nom d'utilisateur
            if (messageRecu.startsWith("Nom : ")) {
                String userName = messageRecu.substring(6);
                server.addClient(clientAddress, clientPort, userName);
                System.out.println("[Serveur] Nouveau client : " + userName);
            } else {
                // Identifier l'expéditeur à partir de la liste des clients
                String senderName = "Inconnu";
                for (ClientInfo client : server.getClients()) {
                    if (client.getAddress().equals(clientAddress) && client.getPort() == clientPort) {
                        senderName = client.getUserName();
                        break;
                    }
                }

                // Gestion des messages privés : si le message commence par "@"
                if (messageRecu.startsWith("@")) {
                    int espaceIndex = messageRecu.indexOf(' ');
                    if (espaceIndex != -1) {
                        String destinataire = messageRecu.substring(1, espaceIndex);
                        String messagePrive = messageRecu.substring(espaceIndex + 1);
                        boolean trouve = false;

                        // Parcourir la liste des clients pour trouver le destinataire
                        for (ClientInfo client : server.getClients()) {
                            if (client.getUserName().equalsIgnoreCase(destinataire)) {
                                String messageEnvoi = "(Privé) " + senderName + " : " + messagePrive;
                                byte[] buffer = messageEnvoi.getBytes();
                                DatagramPacket packetEnvoi = new DatagramPacket(buffer, buffer.length, client.getAddress(), client.getPort());
                                socket.send(packetEnvoi);
                                trouve = true;
                                break;
                            }
                        }
                        if (!trouve) {
                            // Informer l'expéditeur si le destinataire n'a pas été trouvé
                            String messageErreur = "[Serveur] Utilisateur '" + destinataire + "' non trouvé.";
                            byte[] bufferErreur = messageErreur.getBytes();
                            DatagramPacket packetErreur = new DatagramPacket(bufferErreur, bufferErreur.length, clientAddress, clientPort);
                            socket.send(packetErreur);
                        }
                    }
                } else {
                    // Diffusion du message à tous les clients connectés
                    String messageDiffusion = senderName + " : " + messageRecu;
                    byte[] buffer = messageDiffusion.getBytes();

                    for (ClientInfo client : server.getClients()) {
                        DatagramPacket packetEnvoi = new DatagramPacket(buffer, buffer.length, client.getAddress(), client.getPort());
                        socket.send(packetEnvoi);
                    }
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
