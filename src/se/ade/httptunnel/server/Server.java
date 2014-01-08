package se.ade.httptunnel.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class Server {
	private static final int MAX_QUEUED_INCOMING_CONNECTIONS = 100;

	public ExecutorService httpExecutor = Executors.newCachedThreadPool();
	private HttpServer httpServer;
	private int port;

	public Server(int port) {
		this.port = port;
	}

	public static void main(String args[]) {
		int port = 8080;

		if(args.length == 1) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (Exception e) {
				//Use default value.
			}
		}

		try {
			new Server(port).start();
		} catch (Exception e) {
			throw new RuntimeException("Server failed to start.", e);
		}
	}

	private void start() throws IOException {
		httpServer = HttpServer.create(new InetSocketAddress(port), MAX_QUEUED_INCOMING_CONNECTIONS);
		httpServer.setExecutor(httpExecutor);
		httpServer.createContext("/pull", new PullHandler(this));
		httpServer.start();
	}
}
