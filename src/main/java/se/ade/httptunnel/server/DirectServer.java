package se.ade.httptunnel.server;

import se.ade.httptunnel.Config;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class DirectServer {
    private ArrayList<Client> clients = new ArrayList<>();

    public DirectServer(int port) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(port);
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            Client client = new Client(connectionSocket);
            clients.add(client);
        }
    }
    public static void main(String argv[]) throws Exception {
        new DirectServer(Config.SERVER_PORT);
    }
}