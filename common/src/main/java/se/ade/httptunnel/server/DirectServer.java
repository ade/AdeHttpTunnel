package se.ade.httptunnel.server;

import org.json.JSONObject;
import se.ade.httptunnel.Config;
import se.ade.httptunnel.MultiLog;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DirectServer {
    private HashMap<String, Session> sessions = new HashMap<>();
    LinkedList<Socket> incomingConnections = new LinkedList<>();

    public static void main(String argv[]) throws Exception {
        new DirectServer(Config.SERVER_PORT);
    }

    public DirectServer(int port) throws Exception {
        final ServerSocket welcomeSocket = new ServerSocket(port);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Socket connectionSocket = welcomeSocket.accept();
                        incomingConnections.addLast(connectionSocket);
                    } catch (IOException e) {

                    }
                }
            }
        }).start();

        while (true) {
            if(incomingConnections.size() > 0) {
                Socket connectionSocket = incomingConnections.removeFirst();
                MultiLog.v(this, connectionSocket.getInetAddress() + " connected");
                JSONObject params = new DirectReader(connectionSocket.getInputStream()).readJsonObject();
                String sessionId = params.getString("sessionId");
                String clientId = params.getString("clientId");

                Peer peer = findSession(sessionId).findClient(clientId);
                peer.bindSocket(connectionSocket);


            }

            for(Map.Entry<String, Session> entry : sessions.entrySet()) {
                entry.getValue().updateClients();
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                //That's ok
            }
        }
    }


    public Session findSession(String id) {
        if(!sessions.containsKey(id)) {
            //Start session
            sessions.put(id, new Session(this, id));
        }
        return sessions.get(id);
    }
}