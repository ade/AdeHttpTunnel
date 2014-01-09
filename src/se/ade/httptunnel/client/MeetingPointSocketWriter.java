package se.ade.httptunnel.client;

import se.ade.httptunnel.Protocol;

import java.io.*;
import java.net.Socket;

public class MeetingPointSocketWriter {
	private MeetingPointSocket socket;
	private ByteArrayOutputStream outputQueue = new ByteArrayOutputStream();
	private boolean stop = false;

	public MeetingPointSocketWriter(MeetingPointSocket socket) {
		this.socket = socket;
	}

	public void stop() {
		stop = true;
	}

	public void start(String host, int port, String sessionId, ProxyConfiguration proxyConfiguration) {
		try {
			Socket socket;
			if(proxyConfiguration != null) {
				socket = new Socket(proxyConfiguration.host, proxyConfiguration.port);
			} else {
				socket = new Socket(host, port);
			}
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream input = new DataInputStream(socket.getInputStream());

			System.out.println("Starting push request.");
			String cacheInvalidator = System.currentTimeMillis() + "";
			String newline = "\r\n";
			byte[] headers = (
					"PUT http://" + host + ":" + port + "/push?session=" + sessionId + "&cacheInvalidator=" + cacheInvalidator + " HTTP/1.1" + newline +
					"Host: " + host + newline +
					"Content-Type: application/octet-stream" + newline +
					"Content-Length: " + Integer.MAX_VALUE + newline +
					newline
			).getBytes();

			out.write(headers);

			while (!stop) {
				Protocol.FrameType type;
				int length;

				if(outputQueue.size() == 0) {
					type = Protocol.FrameType.JUNK;
					length = Protocol.JUNK_FRAME_SIZE;
				} else {
					type = Protocol.FrameType.JUNK;
					byte[] data = outputQueue.toByteArray();
					outputQueue.reset();

					length = data.length;
					out.write(data);
				}

				byte[] data = new byte[length];

				System.out.println("Upload frame size: " + length);
				System.out.println("Upload frame type: " + type);

				try {
					out.writeInt(type.getValue());
					out.writeInt(length);
					out.write(data);
				} catch (IOException e) {
					System.out.println("Push socket interrupted.");
					stop();
				}

				try {
					Thread.sleep(Protocol.FRAME_DELAY);
				} catch (InterruptedException e) {
					//That's ok
				}
			}

			System.out.println("Push process stopped.");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
