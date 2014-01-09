package se.ade.httptunnel.client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeetingPointToRemoteServiceClient {
	ExecutorService executor = Executors.newFixedThreadPool(2);

	private int servicePort;
	private String serviceHost;
	private String tunnelHost;
	private int tunnelPort;
	private String sessionId;

	public MeetingPointToRemoteServiceClient(String serviceHost, int servicePort, String tunnelHost, int tunnelPort, String sessionId) {
		this.servicePort = servicePort;
		this.serviceHost = serviceHost;
		this.tunnelHost = tunnelHost;
		this.tunnelPort = tunnelPort;
		this.sessionId = sessionId;
	}

	public void start() {
		try {
			Socket localServiceSocket = new Socket(serviceHost, servicePort);
			final MeetingPointSocket meetingPointSocket = new MeetingPointSocket(tunnelHost, tunnelPort, sessionId, null);

			executor.execute(new Runnable() {
				@Override
				public void run() {
					meetingPointSocket.connect();
				}
			});

			new MeetingPointToSocketBinder(localServiceSocket, meetingPointSocket).start();

		} catch (IOException e) {
			System.out.println("Disconnected with exception");
			throw new RuntimeException(e);
		}

		System.out.println("Disconnected");
	}
}
