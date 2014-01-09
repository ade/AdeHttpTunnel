package se.ade.httptunnel.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConsoleSocket {
	public static void start(String hostName, int portNumber) {

		try {
			System.out.println("Connecting");
			Socket kkSocket = new Socket(hostName, portNumber);
			final PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
			System.out.println("Connected");

			new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						String input = System.console().readLine();
						if(input == null || input.equals("q")) {
							return;
						} else {
							System.out.println("Sending: " + input);
							out.println(input);
						}
				}
			}}).run();

			String fromServer;
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server: " + fromServer);
			}
		} catch (IOException e) {
			System.out.println("Disconnected with exception");
			throw new RuntimeException(e);
		}

		System.out.println("Disconnected");
	}

}
