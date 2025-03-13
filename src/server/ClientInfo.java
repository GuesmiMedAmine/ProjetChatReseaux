package server;

import java.net.InetAddress;

public class ClientInfo {
    private InetAddress address;
    private int port;
    private String userName;

    public ClientInfo(InetAddress address, int port, String userName) {
        this.address = address;
        this.port = port;
        this.userName = userName;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return userName + " - " + address + " - " + port;
    }
}
