package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientHandler implements Runnable {
    private DatagramSocket socket;
    private DatagramPacket packetRecu;

    public ClientHandler(DatagramSocket socket, DatagramPacket packetRecu) {
        this.socket = socket;
        this.packetRecu = packetRecu;
    }

    @Override
    public void run() {
        try {
            InetAddress clientAddress = packetRecu.getAddress();
            int clientPort = packetRecu.getPort();
            String messageRecu = new String(packetRecu.getData(), 0, packetRecu.getLength());

            System.out.println("[Serveur] Reçu de " + clientAddress + ":" + clientPort + " -> " + messageRecu);

            String response = "Serveur : " + messageRecu;
            byte[] buffer = response.getBytes();
            DatagramPacket packetEnvoi = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
            socket.send(packetEnvoi);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
