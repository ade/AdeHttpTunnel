package se.ade.httptunnel.client;

import se.ade.httptunnel.InfiniteStream;
import se.ade.httptunnel.Protocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MeetingPointSocketReader {
	private MeetingPointSocket socket;
	private InfiniteStream readBuffer = new InfiniteStream();
	private boolean open = true;

	public MeetingPointSocketReader(MeetingPointSocket socket) {
		this.socket = socket;
	}

	public void start(String host, int port, String sessionId, ProxyConfiguration proxyConfiguration, String clientId) {
		try {
			Socket socket;
			if(proxyConfiguration != null) {
				socket = new Socket(proxyConfiguration.host, proxyConfiguration.port);
			} else {
				socket = new Socket(host, port);
			}

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			DataInputStream input = new DataInputStream(socket.getInputStream());

			System.out.println("Sending request.");
			String cacheInvalidator = System.currentTimeMillis() + "";
			out.println("GET http://" + host + ":" + port + "/pull?session=" + sessionId + "&cacheInvalidator=" + cacheInvalidator + "&clientId=" + clientId + " HTTP/1.1\r\nHost: " + host + "\r\n\r\n");

			//Flush headers.
			boolean foundData = false;
			while(!foundData) {
				byte cr = "\r".getBytes()[0];
				byte lf = "\n".getBytes()[0];

				byte b1 = input.readByte();
				if(b1 == cr) {
					byte b2 = input.readByte();
					byte b3 = input.readByte();
					byte b4 = input.readByte();

					if(b2 == lf && b3 == cr && b4 == lf) {
						foundData = true;
					}
				}
			}

			while (open) {
				Protocol.FrameType type = Protocol.FrameType.parse(input.readInt());
				int length = input.readInt();

				System.out.println("Pulled frame size: " + length);

				byte[] data = new byte[length];
				input.readFully(data);

				switch (type) {
					case JUNK:
						System.out.println("Read " + length + " bytes of junk.");
						break;
					case DATA:
						System.out.println("Read " + length + " bytes of data.");
						readBuffer.write(data);
						break;
					case END:
						System.out.println("Reached end of session.");
						open = false;
					default:
						throw new IOException("Unknown response type!");
				}
			}

			System.out.println("Pull reached end.");
		} catch (IOException e) {
			open = false;
			throw new RuntimeException(e);
		}
	}

	public boolean hasData() {
		try {
			return readBuffer.available() > 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] getData() {
		return readBuffer.readAll();
	}

	public boolean isOpen() {
		return open;
	}
}
