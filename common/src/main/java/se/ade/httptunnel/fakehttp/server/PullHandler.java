package se.ade.httptunnel.fakehttp.server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;

public class PullHandler extends BaseHandler {
	FakeHttpServer server;

	public PullHandler(FakeHttpServer server) {
		this.server = server;
	}

	@Override
	public void handle(HttpExchange exchange, HashMap<String, String> queryString) throws IOException {
		boolean sendError = true;

		String sessionId = queryString.get("session");
		String clientId = queryString.get("clientId");
		if(sessionId != null) {
			sendError = false;

			HttpSession session = server.findOrStartSession(sessionId);
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
