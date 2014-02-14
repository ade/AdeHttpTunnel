package se.ade.httptunnel.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.*;

public class Server {
	private static final int MAX_QUEUED_INCOMING_CONNECTIONS = 100;
	private HashMap<String, Session> sessions = new HashMap<String, Session>();

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

	public void start() {
		try {
			httpServer = HttpServer.create(new InetSocketAddress(port), MAX_QUEUED_INCOMING_CONNECTIONS);
			httpServer.setExecutor(httpExecutor);
			httpServer.createContext("/pull", new PullHandler(this));
			httpServer.createContext("/push", new PushHandler(this));
			httpServer.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized Session findOrStartSession(String sessionId) {
		if(!sessions.containsKey(sessionId)) {
			return startSession(sessionId);
		} else {
			return sessions.get(sessionId);
		}
	}

	public Session startSession(String sessionId) {
		Session session = new Session(sessionId);
		sessions.put(sessionId, session);
		return session;
	}
}
