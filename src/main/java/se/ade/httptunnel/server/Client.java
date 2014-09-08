package se.ade.httptunnel.server;

import java.net.Socket;


public class Client {
    private Socket socket;

    public Client(Socket socket) {
        this.socket = socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
