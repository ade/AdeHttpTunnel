package se.ade.httptunnel.server;

import com.sun.net.httpserver.HttpExchange;
import se.ade.httptunnel.Protocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class PullHandler extends BaseHandler {
	Server server;

	public PullHandler(Server server) {
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
			Puller puller = new Puller(exchange, clientId, session);
			session.pullers.put(clientId, puller);
			puller.start();
		}

		if(sendError) {
			respondWithBadRequest(exchange);
		}

		exchange.close();
	}
}
