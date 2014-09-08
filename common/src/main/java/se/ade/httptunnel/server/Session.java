package se.ade.httptunnel.server;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private DirectServer server;
    private HashMap<String, Peer> clients = new HashMap<>();
    private String sessionId;

    public Session(DirectServer server, String sessionId) {
        this.server = server;
    }

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    public Peer findClient(String id) {
        if(!clients.containsKey(id)) {
            clients.put(id, new Peer(server, id, this));
        }
        return clients.get(id);
    }

    public void send(Peer fromPeer, byte[] message) {
        for(Map.Entry<String, Peer> clientEntry : clients.entrySet()) {
            Peer peer = clientEntry.getValue();
            if(peer != fromPeer) {
                peer.onReceive(message, fromPeer);
            }
        }
    }

    public void updateClients() {
        for (Map.Entry<String, Peer> clientEntry : clients.entrySet()) {
            clientEntry.getValue().update();
        }
    }
}
