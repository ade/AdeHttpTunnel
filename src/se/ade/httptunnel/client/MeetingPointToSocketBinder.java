package se.ade.httptunnel.client;

import se.ade.httptunnel.InfiniteStream;
import se.ade.httptunnel.MultiLog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: adrnil
 * Date: 2014-01-09
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */
public class MeetingPointToSocketBinder {
	private Socket socket;
	private MeetingPointSocket meetingPointSocket;

	public MeetingPointToSocketBinder(Socket socket, MeetingPointSocket meetingPointSocket) {
		this.socket = socket;
		this.meetingPointSocket = meetingPointSocket;
	}

	public void start() throws IOException {
		InfiniteStream proxyInput = meetingPointSocket.getInputStream();
		InfiniteStream proxyOutput = meetingPointSocket.getOutputStream();
		DataInputStream socketInput = new DataInputStream(socket.getInputStream());
		DataOutputStream socketOutput = new DataOutputStream(socket.getOutputStream());

		while(!socket.isClosed() && meetingPointSocket.isConnected()) {
			if(proxyInput.available() > 0) {
				MultiLog.v("AdeHttpTunnel", "Relaying " + proxyInput.available() + " bytes of tunnel received data...");
				byte[] data = proxyInput.readAll();
				socketOutput.write(data);
				socketOutput.flush();
			}

			proxyOutput.consumeAvailable(socketInput);

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				//Ignore
			}
		}
	}
}
