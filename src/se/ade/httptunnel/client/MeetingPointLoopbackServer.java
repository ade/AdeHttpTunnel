package se.ade.httptunnel.client;

import se.ade.httptunnel.Config;
import se.ade.httptunnel.InfiniteStream;
import se.ade.httptunnel.MultiLog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeetingPointLoopbackServer {
	ExecutorService executor = Executors.newSingleThreadExecutor();

	private int localPort;
	private String host;
	private int remotePort;
	private String sessionId;

	public MeetingPointLoopbackServer(int localPort, String host, int remotePort, String sessionId) {
		this.localPort = localPort;
		this.host = host;
		this.remotePort = remotePort;
		this.sessionId = sessionId;
	}

	public void start() {
		try {
			ServerSocket serverSocket = new ServerSocket(localPort);
			Socket clientSocket = serverSocket.accept();

			final MeetingPointSocket meetingPointSocket = new MeetingPointSocket(host, remotePort, sessionId, Config.PROXY_CONFIG);

		 	executor.execute(new Runnable() {
				@Override
				public void run() {
					MultiLog.v("AdeHttpTunnel", "Client connected, opening tunnel...");
					meetingPointSocket.connect();
				}
			});

			new MeetingPointToSocketBinder(clientSocket, meetingPointSocket).start();

			MultiLog.v("AdeHttpTunnel", "Shutting down loopback server.");
		} catch(IOException e) {
			System.exit(0);
			throw new RuntimeException(e);
		}
	}
}
