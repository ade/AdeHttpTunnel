package se.ade.httptunnel.client;

import se.ade.httptunnel.Protocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: adrnil
 * Date: 2014-01-08
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */
public class MeetingPointSocketReader {
	private MeetingPointSocket socket;

	public MeetingPointSocketReader(MeetingPointSocket socket) {
		this.socket = socket;
	}

	public void start(String host, int port, String sessionId) {
		try {
			Socket socket = new Socket(host, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			DataInputStream input = new DataInputStream(socket.getInputStream());

			System.out.println("Sending request.");
			out.println("GET /pull?session=" + sessionId + " HTTP/1.1\r\nHost: " + host + "\r\n\r\n");

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

			boolean read = true;
			while (read) {
				Protocol.FrameType type = Protocol.FrameType.parse(input.readInt());
				int length = input.readInt();

				System.out.println("Frame size: " + length);

				byte[] data = new byte[length];
				input.readFully(data);

				switch (type) {
					case JUNK:
						System.out.println("Read " + length + " bytes of junk.");
						break;
					case DATA:
						System.out.println("Read " + length + " bytes of data.");
						break;
					case END:
						System.out.println("Reached end of session.");
						read = false;
					default:
						throw new IOException("Unknown response type!");
				}
			}

			System.out.println("Pull reached end.");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
