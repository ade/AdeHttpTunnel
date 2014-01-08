package se.ade.httptunnel.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import se.ade.httptunnel.Protocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;

public class PullHandler implements HttpHandler {
	Server server;

	public PullHandler(Server server) {
		this.server = server;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String query = exchange.getRequestURI().getRawQuery();
		HashMap<String, String> parameters = new HashMap<String, String>();
		boolean sendError = true;

		if (query != null && !query.equals("")) {
			String[] paramList = query.split("\\&");
			for (String param : paramList) {
				String[] split = param.split("\\=", 2);
				if (split.length == 1) {
					continue;
				}
				parameters.put(URLDecoder.decode(split[0]), URLDecoder.decode(split[1]));
			}

			String session = parameters.get("session");
			if(session != null) {
				sendError = false;
				System.out.println("Handling request: " + exchange.getRequestURI().toString());

				exchange.sendResponseHeaders(200, Integer.MAX_VALUE);
				exchange.getResponseHeaders().add("Content-Type", "application/octet-stream");
				OutputStream responseStream = exchange.getResponseBody();
				DataOutputStream output = new DataOutputStream(responseStream);
				boolean open = true;
				while (open) {
					System.out.println("Writing response...");

					try {
						byte[] response = new byte[Protocol.SERVER_PULL_JUNK_FRAME_SIZE];
						output.writeInt(Protocol.FrameType.JUNK.getValue());
						output.writeInt(response.length);
						output.write(response);

						try {
							Thread.sleep(Protocol.SERVER_PULL_FRAME_DELAY);
						} catch (InterruptedException e) {
							//That's ok
						}
					} catch (IOException e) {
						open = false;
						System.out.println("Socket closed.");
					}
				}
			}


		}

		if(sendError) {
			byte[] response = "<html><body>Bad request.</body><html>".getBytes();
			exchange.sendResponseHeaders(401, response.length);
			exchange.getResponseHeaders().add("Content-Type", "text/html");
			OutputStream responseStream = exchange.getResponseBody();
			responseStream.write(response);
		}

		exchange.close();
	}
}
