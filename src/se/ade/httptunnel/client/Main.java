package se.ade.httptunnel.client;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class Main {
	private enum Mode {
		LoopbackServer,
		HelloWorldServer, ConsoleSocket
	}

	public static void main(String[] argsArray) {
		Queue<String> args = new ArrayDeque<String>(Arrays.asList(argsArray));
		String host = null;
		Integer port = null;
		Integer listenPort = null;
		String sessionId = null;
		Mode mode = null;

		while (args.size() > 0) {
			String option = args.poll();

			if (!option.startsWith("-")) {
				throw new RuntimeException("Unexpected value \"" + option + "\", see -help for info.");
			} else if(option.equalsIgnoreCase("-h")) {
				String value = args.poll();
				String[] split = value.split("\\:");
				host = split[0];
				port = Integer.parseInt(split[1]);
			} else if (option.equalsIgnoreCase("-l")) {
				listenPort = Integer.parseInt(args.poll());
				mode = Mode.LoopbackServer;
			} else if (option.equalsIgnoreCase("-s")) {
				sessionId = args.poll();
			} else if(option.equalsIgnoreCase("-help")) {
				System.out.println("-h host:post -l listenport -s sessionid");
				System.exit(0);
			} else if(option.equalsIgnoreCase("-c")) {
				mode = Mode.ConsoleSocket;
			} else if(option.equalsIgnoreCase("-helloworldserver")) {
				listenPort = Integer.parseInt(args.poll());
				mode = Mode.HelloWorldServer;
			} else {
				throw new RuntimeException("Unrecognized option \"" + option + "\", see -help for info.");
			}
		}

		if(mode != null) {
			switch (mode) {
				case LoopbackServer:
					if(listenPort != null && host != null && port != null && sessionId != null) {
						System.out.println("Starting loopback forwarder at local port " + listenPort + " forwarding to meeting point " + host + ":" + port + " for session id: " + sessionId);

						new MeetingPointLoopbackServer(listenPort, host, port, sessionId).start();
					}
					break;
				case ConsoleSocket:
					System.out.println("Opening a test connection to " + host + ":" + port + "...");
					new ConsoleSocket(host, port).start();
					break;
				case HelloWorldServer:
					System.out.println("Starting a hello world server on port " + listenPort + ".");
					new HelloWorldServer(listenPort).start();
					break;
			}
		}

	}
}
