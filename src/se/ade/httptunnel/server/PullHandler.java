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

		String session = queryString.get("session");
		if(session != null) {
			sendError = false;
			exchange.sendResponseHeaders(200, Integer.MAX_VALUE);
			exchange.getResponseHeaders().add("Content-Type", "application/octet-stream");
			OutputStream responseStream = exchange.getResponseBody();
			DataOutputStream output = new DataOutputStream(responseStream);
			boolean open = true;
			while (open) {
				System.out.println("Writing response...");

				try {
					byte[] response = new byte[Protocol.JUNK_FRAME_SIZE];
					output.writeInt(Protocol.FrameType.JUNK.getValue());
					output.writeInt(response.length);
					output.write(response);

					try {
						Thread.sleep(Protocol.FRAME_DELAY);
					} catch (InterruptedException e) {
						//That's ok
					}
				} catch (IOException e) {
					open = false;
					System.out.println("Pull socket was interrupted.");
				}
			}
		}

		if(sendError) {
			respondWithBadRequest(exchange);
		}

		exchange.close();
	}
}
