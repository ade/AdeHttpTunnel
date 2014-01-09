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

		String session = queryString.get("session");
		if(session != null) {
			sendError = false;

			DataInputStream input = new DataInputStream(exchange.getRequestBody());

			boolean read = true;
			while (read) {
				Protocol.FrameType type = Protocol.FrameType.parse(input.readInt());
				int length = input.readInt();

				System.out.println("Incoming push frame size: " + length);

				byte[] data = new byte[length];
				input.readFully(data);

				switch (type) {
					case JUNK:
						System.out.println("Push exchange: Read " + length + " bytes of junk.");
						break;
					case DATA:
						System.out.println("Push exchange: Read " + length + " bytes of data.");
						break;
					case END:
						System.out.println("Reached end of push request.");
						read = false;
					default:
						throw new IOException("Unknown push content type!");
				}
			}
		}

		if(sendError) {
			respondWithBadRequest(exchange);
		}

		System.out.println("Closing push exchange.");
		exchange.close();
	}
}
