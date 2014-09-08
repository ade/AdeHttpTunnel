package se.ade.httptunnel.server;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private DirectServer server;
    private HashMap<String, Client> clients = new HashMap<>();
    private String sessionId;

    public Session(DirectServer server, String sessionId) {
        this.server = server;
    }

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    public Client findClient(String id) {
        if(!clients.containsKey(id)) {
            clients.put(id, new Client(server, id, this));
        }
        return clients.get(id);
    }

    public void send(Client fromClient, byte[] message) {
        for(Map.Entry<String, Client> clientEntry : clients.entrySet()) {
            Client client = clientEntry.getValue();
            if(client != fromClient) {
                client.onReceive(message, fromClient);
            }
        }
    }

    public void updateClients() {
        for (Map.Entry<String, Client> clientEntry : clients.entrySet()) {
            clientEntry.getValue().update();
        }
    }
}
