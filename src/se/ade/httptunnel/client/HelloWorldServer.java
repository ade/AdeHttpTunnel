package se.ade.httptunnel.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HelloWorldServer {
	private int port;

	public HelloWorldServer(int port) {
		this.port = port;
	}

	public void start() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(true) {
				Socket clientSocket = serverSocket.accept();

				clientSocket.getOutputStream().write("Hello World!\r\n".getBytes());
				clientSocket.getOutputStream().flush();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
