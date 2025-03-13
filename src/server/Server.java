package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int port;
    private DatagramSocket serverSocket;
    private List<ClientInfo> clients = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new DatagramSocket(port);
            System.out.println("[Serveur] Démarré sur le port " + port);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packetRecu = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packetRecu);

                // Démarrer un thread pour gérer le message reçu
                new Thread(new ClientHandler(this, packetRecu)).start();

                // Afficher la liste des clients connectés
                printClientList();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addClient(InetAddress address, int port, String userName) {
        boolean exists = false;
        for (ClientInfo client : clients) {
            if (client.getAddress().equals(address) && client.getPort() == port) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            ClientInfo nouveauClient = new ClientInfo(address, port, userName);
            clients.add(nouveauClient);
            System.out.println("[Serveur] Nouveau client : " + nouveauClient);
        }
    }

    public synchronized List<ClientInfo> getClients() {
        return clients;
    }

    public synchronized void printClientList() {
        System.out.println("[Serveur] Liste des clients connectés :");
        for (ClientInfo client : clients) {
            System.out.println(client);
        }
    }
}
