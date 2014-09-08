package se.ade.httptunnel.fakehttp.server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;

public class PushHandler extends BaseHandler {
	FakeHttpServer server;

	public PushHandler(FakeHttpServer server) {
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
