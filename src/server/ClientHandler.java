package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // Prépare les flux de lecture/écriture
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Demande au client de s'identifier
            out.println("Bienvenue ! Entrez votre nom :");
            clientName = in.readLine(); // le client envoie son nom
            out.println("Bonjour " + clientName + " ! Tapez vos messages (ou 'quit' pour sortir).");

            // Préviens tout le monde de l'arrivée de ce client
            server.broadcast("[Serveur] " + clientName + " a rejoint la conversation.");

            // Boucle de lecture des messages
            String message;
            while ((message = in.readLine()) != null) {
                // Si l'utilisateur veut quitter
                if (message.equalsIgnoreCase("quit")) {
                    out.println("Au revoir !");
                    break;
                }
                // Sinon, on diffuse le message à tous
                server.broadcast("[" + clientName + "] " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Nettoyage quand le client se déconnecte
            fermerConnexion();
        }
    }

    private void fermerConnexion() {
        try {
            // Retirer ce client de la liste
            server.removeClient(this);

            // Prévenir tout le monde
            if (clientName != null) {
                server.broadcast("[Serveur] " + clientName + " s'est déconnecté.");
            }

            // Fermer les flux
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Envoi d'un message vers CE client
    public void sendMessage(String msg) {
        out.println(msg);
    }
}
