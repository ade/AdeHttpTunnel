package se.ade.httptunnel.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class MeetingPointLoopbackServer {
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

	public static void main(String[] argsArray) {
		Queue<String> args = new ArrayDeque<String>(Arrays.asList(argsArray));
		String host = null;
		Integer port = null;
		Integer listenPort = null;
		String sessionId = null;

		while (args.size() > 0) {
			String option = args.poll();

			if (!option.startsWith("-")) {
				throw new RuntimeException("Unexpected value \"" + option + "\", see -help for info.");
			} else if(option.equalsIgnoreCase("-h")) {
				String value = args.poll();
				String[] split = value.split("\\:");
				host = split[0];
				port = Integer.parseInt(split[1]);
			} else if (option.equalsIgnoreCase("-l")) {
				listenPort = Integer.parseInt(args.poll());
			} else if (option.equalsIgnoreCase("-s")) {
				sessionId = args.poll();
			} else if(option.equalsIgnoreCase("-help")) {
				System.out.println("-h host:post -l listenport -s sessionid");
				System.exit(0);
			} else {
				throw new RuntimeException("Unrecognized option \"" + option + "\", see -help for info.");
			}
		}

		if(listenPort != null && host != null && port != null && sessionId != null) {
			System.out.println("Starting loopback forwarder at local port " + listenPort + " forwarding to meeting point " + host + ":" + port + " for session id: " + sessionId);

			new MeetingPointLoopbackServer(listenPort, host, port, sessionId).start();
		}
	}

	public void start() {
		try {
			ServerSocket serverSocket = new ServerSocket(localPort);
			Socket clientSocket = serverSocket.accept();

			DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
			MeetingPointSocket meetingPointSocket = new MeetingPointSocket(host, remotePort, sessionId, null);
			while(!clientSocket.isClosed()) {

			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
