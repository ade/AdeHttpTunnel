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

	public void start(String host, int port, String sessionId) {
		try {
			Socket socket = new Socket(host, port);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream input = new DataInputStream(socket.getInputStream());

			System.out.println("Starting push request.");
			byte[] headers = ("GET /push?session=" + sessionId + " HTTP/1.1\r\nHost: " + host + "\r\n\r\n").getBytes();
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

				out.writeInt(type.getValue());
				out.write(data);
			}

			System.out.println("Push process stopped.");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
