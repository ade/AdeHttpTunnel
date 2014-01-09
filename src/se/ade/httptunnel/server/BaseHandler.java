package se.ade.httptunnel.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;

public abstract class BaseHandler implements HttpHandler {
	public HashMap<String, String> getQueryString(String fullQueryString) {
		HashMap<String, String> parameters = new HashMap<String, String>();

		if (fullQueryString != null && fullQueryString.contains("=")) {
			String[] paramList = fullQueryString.split("\\&");
			for (String param : paramList) {
				String[] split = param.split("\\=", 2);
				if (split.length == 1) {
					continue;
				}
				parameters.put(URLDecoder.decode(split[0]), URLDecoder.decode(split[1]));
			}


		}

		return parameters;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		System.out.println("Handling request: " + exchange.getRequestURI().toString());

		handle(exchange, getQueryString(exchange.getRequestURI().getRawQuery()));
	}

	protected void respondWithBadRequest(HttpExchange exchange) throws IOException {
		byte[] response = "<html><body>Bad request.</body><html>".getBytes();
		exchange.sendResponseHeaders(401, response.length);
		exchange.getResponseHeaders().add("Content-Type", "text/html");
		OutputStream responseStream = exchange.getResponseBody();
		responseStream.write(response);
	}

	public abstract void handle(HttpExchange exchange, HashMap<String, String> queryString) throws IOException;
}
