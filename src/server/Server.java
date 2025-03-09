package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    private int port;
    private ServerSocket serverSocket;

    // Liste synchronisée de tous les clients connectés
    private List<ClientHandler> clients;

    public Server(int port) {
        this.port = port;
        this.clients = Collections.synchronizedList(new ArrayList<>());
    }

    public void start() {
        try {
            // Création du ServerSocket
            serverSocket = new ServerSocket(port);
            System.out.println("[Serveur] Démarré sur le port " + port);

            // Boucle infinie : on accepte les nouvelles connexions
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Serveur] Nouveau client connecté : " + clientSocket.getRemoteSocketAddress());

                // On crée un ClientHandler (thread) pour chaque nouveau client
                ClientHandler handler = new ClientHandler(clientSocket, this);
                clients.add(handler);
                handler.start(); // Lancement du thread
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Diffuse un message à tous les clients connectés.
     */
    public void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler ch : clients) {
                ch.sendMessage(message);
            }
        }
    }

    /**
     * Retire un client de la liste (lorsqu’il se déconnecte).
     */
    public void removeClient(ClientHandler clientHandler) {
        synchronized (clients) {
            clients.remove(clientHandler);
        }
    }
}
