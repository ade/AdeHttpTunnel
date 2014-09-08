package se.ade.httptunnel.fakehttp.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.*;

public class FakeHttpServer {
	private static final int MAX_QUEUED_INCOMING_CONNECTIONS = 100;
	private HashMap<String, HttpSession> sessions = new HashMap<String, HttpSession>();

	public ExecutorService httpExecutor = Executors.newCachedThreadPool();
	private HttpServer httpServer;
	private int port;

	public FakeHttpServer(int port) {
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
			new FakeHttpServer(port).start();
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

	public synchronized HttpSession findOrStartSession(String sessionId) {
		if(!sessions.containsKey(sessionId)) {
			return startSession(sessionId);
		} else {
			return sessions.get(sessionId);
		}
	}

	public HttpSession startSession(String sessionId) {
		HttpSession session = new HttpSession(sessionId);
		sessions.put(sessionId, session);
		return session;
	}
}
