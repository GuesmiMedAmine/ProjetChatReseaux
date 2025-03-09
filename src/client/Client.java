package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String host;
    private int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner scanner;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        scanner = new Scanner(System.in);
        try {
            // Connexion au serveur
            socket = new Socket(host, port);
            System.out.println("[Client] Connecté au serveur " + host + ":" + port);

            // Prépare les flux
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Thread pour lire en continu les messages venant du serveur
            Thread readThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    System.out.println("[Client] Connexion fermée par le serveur.");
                }
            });
            readThread.start();

            // Boucle pour envoyer des messages au serveur
            while (true) {
                String userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("quit")) {
                    // On envoie "quit" puis on arrête
                    out.println("quit");
                    break;
                }
                // Sinon on envoie le message
                out.println(userInput);
            }

            // Fermeture
            close();
            readThread.join(); // Attend la fin du thread de lecture si besoin

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            if (out != null) out.close();
            if (in != null)  in.close();
            if (socket != null && !socket.isClosed()) socket.close();
            if (scanner != null) scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
