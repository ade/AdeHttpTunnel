package se.ade.httptunnel.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsoleSocket {
	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	private String hostName;
	private int portNumber;
	private boolean connected = true;

	public ConsoleSocket(String hostName, int portNumber) {
		this.hostName = hostName;
		this.portNumber = portNumber;
	}

	public void start() {
		try {
			System.out.println("Connecting");
			Socket kkSocket = new Socket(hostName, portNumber);
			final PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
			System.out.println("Connected");

			executorService.execute(new Runnable() {
				@Override
				public void run() {
					while(connected) {
						String input = System.console().readLine();
						if(input != null && !input.equals("")) {
							System.out.println("Sending: " + input);
							out.println(input);
						}
				}
			}});

			String fromServer;
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server: " + fromServer);
			}
		} catch (IOException e) {
			connected = false;
			System.out.println("Disconnected with exception");
			throw new RuntimeException(e);
		}

		connected = false;
		System.out.println("Disconnected");
	}

}
