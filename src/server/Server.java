package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {
    private int port;
    private DatagramSocket socket;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            socket = new DatagramSocket(port);
            System.out.println("[Serveur] Démarré sur le port " + port);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packetRecu = new DatagramPacket(buffer, buffer.length);
                socket.receive(packetRecu);

                new Thread(new ClientHandler(socket, packetRecu)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
