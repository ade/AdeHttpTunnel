package se.ade.httptunnel.client;

import se.ade.httptunnel.InfiniteStream;
import se.ade.httptunnel.Protocol;

import java.io.*;
import java.net.Socket;

public class MeetingPointSocketWriter {
	private MeetingPointSocket socket;
	private boolean open = true;
	private InfiniteStream sendBuffer = new InfiniteStream();
	private DataOutputStream uploadStream;

	public MeetingPointSocketWriter(MeetingPointSocket socket) {
		this.socket = socket;
	}

	public void stop() {
		open = false;
	}

	public void send(byte[] data) {
		System.out.println("Writing " + data.length + " bytes for upload..");
		sendBuffer.write(data);
		writeFrame();
	}

	public void start(String host, int port, String sessionId, ProxyConfiguration proxyConfiguration, String clientId) {
		try {
			Socket socket;
			if(proxyConfiguration != null) {
				socket = new Socket(proxyConfiguration.host, proxyConfiguration.port);
			} else {
				socket = new Socket(host, port);
			}
			uploadStream = new DataOutputStream(socket.getOutputStream());

			System.out.println("Starting push request.");
			String cacheInvalidator = System.currentTimeMillis() + "";
			String newline = "\r\n";
			byte[] headers = (
					"PUT http://" + host + ":" + port + "/push?session=" + sessionId + "&cacheInvalidator=" + cacheInvalidator + "&clientId=" + clientId + " HTTP/1.1" + newline +
					"Host: " + host + newline +
					"Content-Type: application/octet-stream" + newline +
					"Content-Length: " + Integer.MAX_VALUE + newline +
					newline
			).getBytes();

			uploadStream.write(headers);

			while (open) {
				writeFrame();

				try {
					Thread.sleep(Protocol.JUNK_FRAME_DELAY);
				} catch (InterruptedException e) {
					//That's ok
				}
			}

			System.out.println("Push process stopped.");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void writeFrame() {

		Protocol.FrameType type;
		int length;
		byte[] data;

		if(sendBuffer.available() == 0) {
			type = Protocol.FrameType.JUNK;
			length = Protocol.JUNK_FRAME_SIZE;
			data = new byte[length];
		} else {
			type = Protocol.FrameType.DATA;
			data = sendBuffer.read(Protocol.MAX_FRAME_SIZE);
			length = data.length;
		}

		System.out.println("Upload frame size: " + length);
		System.out.println("Upload frame type: " + type);

		try {
			uploadStream.writeInt(type.getValue());
			uploadStream.writeInt(length);
			uploadStream.write(data);
		} catch (IOException e) {
			System.out.println("Push socket interrupted.");
			stop();
		}

	}

	public boolean isOpen() {
		return open;
	}
}
