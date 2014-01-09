package se.ade.httptunnel.server;

import com.sun.net.httpserver.HttpExchange;
import se.ade.httptunnel.InfiniteStream;
import se.ade.httptunnel.Protocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Puller {
	private HttpExchange exchange;
	private String clientId;
	private InfiniteStream brokeredDataBuffer = new InfiniteStream();
	private boolean open = true;
	private Session session;

	public Puller(HttpExchange exchange, String clientId, Session session) {
		this.exchange = exchange;
		this.clientId = clientId;
		this.session = session;
	}

	public void start() throws IOException {
		try {
			exchange.sendResponseHeaders(200, Integer.MAX_VALUE);
			exchange.getResponseHeaders().add("Content-Type", "application/octet-stream");

			while (open) {
				writeFrame();

				try {
					Thread.sleep(Protocol.FRAME_DELAY);
				} catch (InterruptedException e) {
					//That's ok
				}
			}
		} catch (IOException e) {
			System.out.println("Pull connection was interrupted in the initialization phase.");
			close();
		}
	}

	public synchronized void onBrokeredData(byte[] data) throws IOException {
		brokeredDataBuffer.write(data);
		while(brokeredDataBuffer.available() > 0) {
			writeFrame();
		}
	}

	private synchronized void writeFrame() {
		try {
			OutputStream responseStream = exchange.getResponseBody();
			DataOutputStream output = new DataOutputStream(responseStream);

			if(brokeredDataBuffer.available() > 0) {
				byte[] response = brokeredDataBuffer.read(Protocol.MAX_FRAME_SIZE);
				System.out.println("Writing " + response.length + " bytes of data...");
				output.writeInt(Protocol.FrameType.DATA.getValue());
				output.writeInt(response.length);
				output.write(response);
			} else {
				byte[] response = new byte[Protocol.JUNK_FRAME_SIZE];
				System.out.println("Writing " + response.length + " bytes of junk...");
				output.writeInt(Protocol.FrameType.JUNK.getValue());
				output.writeInt(response.length);
				output.write(response);
			}

		} catch (IOException e) {
			System.out.println("Pull socket was interrupted.");
			close();
		}
	}

	public void close() {
		open = false;
		session.pullers.remove(this.clientId);
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
