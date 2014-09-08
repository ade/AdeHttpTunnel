package se.ade.httptunnel.server;

import se.ade.httptunnel.InfiniteStream;
import se.ade.httptunnel.MultiLog;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Peer {
    private Socket socket;
    private DataOutputStream outputStream;
    private DirectReader inputStream;
    private DirectServer server;
    private String id;
    private Session session;
    private InfiniteStream receiveBuffer = new InfiniteStream();

    public Peer(DirectServer server, String id, Session session) {
        this.server = server;
        this.session = session;
        this.id = id;
    }

    public void bindSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.inputStream = new DirectReader(socket.getInputStream());

        if(receiveBuffer.available() > 0) {
            byte[] b = receiveBuffer.readAll();
            onReceive(b, null);
        }
    }


    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void onReceive(byte[] message, Peer fromPeer) {
        try {
            if(this.socket.isClosed() || !this.socket.isConnected()) {
                throw new IOException("Closed");
            }
            this.outputStream.write(message);
            this.outputStream.flush();
        } catch (IOException e) {
            MultiLog.d(this, "Could not write message to clientId " + this.id);
            MultiLog.d(this, "Buffering " + message.length + " bytes");
            receiveBuffer.write(message);
        }

    }

    public void update() {
        try {
            int available = inputStream.available();

            if (available > 0) {
                MultiLog.v(this, "Relaying " + inputStream.available() + " bytes of tunnel received data...");
                byte[] data = new byte[available];
                inputStream.readFully(data);
                session.send(this, data);
            }
        } catch (IOException e) {

        }
    }
}
