package se.ade.httptunnel.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import se.ade.httptunnel.Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class PushHandler extends BaseHandler {
	Server server;

	public PushHandler(Server server) {
		this.server = server;
	}

	@Override
	public void handle(HttpExchange exchange, HashMap<String, String> queryString) throws IOException {
		boolean sendError = true;

		String sessionId = queryString.get("session");
		String clientId = queryString.get("clientId");

		if(sessionId != null) {
			sendError = false;
			Session session = server.findOrStartSession(sessionId);
			Pusher pusher = new Pusher(exchange, clientId, session);
			session.pushers.put(clientId, pusher);
			pusher.start();
		}

		if(sendError) {
			respondWithBadRequest(exchange);
		}

		System.out.println("Closing push exchange.");
		exchange.close();
	}
}
