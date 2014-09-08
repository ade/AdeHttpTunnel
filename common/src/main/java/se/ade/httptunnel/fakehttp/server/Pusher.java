package se.ade.httptunnel.fakehttp.server;

import com.sun.net.httpserver.HttpExchange;
import se.ade.httptunnel.fakehttp.Protocol;

import java.io.DataInputStream;
import java.io.IOException;

public class Pusher {
	private HttpExchange exchange;
	private String clientId;
	private HttpSession session;
	boolean read = true;

	public Pusher(HttpExchange exchange, String clientId, HttpSession session) {
		this.exchange = exchange;
		this.clientId = clientId;
		this.session = session;
	}

	public void start() {
		try {
			DataInputStream input = new DataInputStream(exchange.getRequestBody());

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
						session.onData(data, this.clientId);
						break;
					case END:
						System.out.println("Reached end of push request.");
						read = false;
					default:
						throw new IOException("Unknown push content type!");
				}
			}
		} catch (IOException e) {
			System.out.println("Pusher connection interrupted.");
			close();
		}
	}
	public void close() {
		read = false;
		session.pushers.remove(this.clientId);
	}
}
