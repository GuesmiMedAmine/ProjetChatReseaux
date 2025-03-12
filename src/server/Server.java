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

    // Liste des clients connectés
    private List<InetAddress> clientAddresses = new ArrayList<>();
    private List<Integer> clientPorts = new ArrayList<>();
    private List<String> clientNames = new ArrayList<>();

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

                // Démarrer un thread pour gérer le message
                new Thread(new ClientHandler(this, packetRecu)).start();

                // Afficher la liste des clients connectés
                printClientList();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ajouter un client à la liste
    public synchronized void addClient(InetAddress address, int port, String name) {
        if (!clientAddresses.contains(address) || !clientPorts.contains(port)) {
            clientAddresses.add(address);
            clientPorts.add(port);
            clientNames.add(name);

            // Afficher l'adresse et le port du client
            System.out.println("[Serveur] Nouveau client : " + name + " - Adresse : " + address + " - Port : " + port);
        }
    }

    // Récupérer la liste des clients
    public synchronized List<InetAddress> getClientAddresses() {
        return clientAddresses;
    }

    public synchronized List<Integer> getClientPorts() {
        return clientPorts;
    }

    public synchronized List<String> getClientNames() {
        return clientNames;
    }

    // Afficher la liste des clients connectés
    public synchronized void printClientList() {
        System.out.println("[Serveur] Liste des clients connectés :");
        for (int i = 0; i < clientAddresses.size(); i++) {
            System.out.println(clientNames.get(i) + " - Adresse : " + clientAddresses.get(i) + " - Port : " + clientPorts.get(i));
        }
    }
}
