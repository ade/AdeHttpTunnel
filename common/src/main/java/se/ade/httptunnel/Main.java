package se.ade.httptunnel;

import se.ade.httptunnel.fakehttp.client.MeetingPointLoopbackServer;
import se.ade.httptunnel.fakehttp.client.MeetingPointToRemoteServiceClient;
import se.ade.httptunnel.fakehttp.server.FakeHttpServer;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class Main {
	private enum Mode {
		LoopbackServer,
		HelloWorldServer,
        RemoteService,
        TunnelServer,
        ConsoleSocket
	}

	public static void main(String[] argsArray) {
		Queue<String> args = new ArrayDeque<String>(Arrays.asList(argsArray));
		String host = null;
		Integer port = null;
		Integer listenPort = null;
		String sessionId = null;
		String tunnelHost = null;
		Integer tunnelPort = null;
		Mode mode = null;

		while (args.size() > 0) {
			String option = args.poll();

			if (!option.startsWith("-")) {
				throw new RuntimeException("Unexpected value \"" + option + "\", see -help for info.");
			} else if (option.equalsIgnoreCase("-l")) {
				listenPort = Integer.parseInt(args.poll());
				mode = Mode.LoopbackServer;
			} else if (option.equalsIgnoreCase("-s")) {
				sessionId = args.poll();
			} else if(option.equalsIgnoreCase("-help")) {
				MultiLog.v("AdeHttpTunnel", "-h host:post -l listenport -s sessionid");
				System.exit(0);
			} else if(option.equalsIgnoreCase("-c")) {
				mode = Mode.ConsoleSocket;
				String value = args.poll();
				String[] split = value.split("\\:");
				host = split[0];
				port = Integer.parseInt(split[1]);
			} else if(option.equalsIgnoreCase("-helloworldserver")) {
				listenPort = Integer.parseInt(args.poll());
				mode = Mode.HelloWorldServer;
			} else if(option.equalsIgnoreCase("-service")) {
				mode = Mode.RemoteService;
				String value = args.poll();
				String[] split = value.split("\\:");
				host = split[0];
				port = Integer.parseInt(split[1]);
			} else if(option.equalsIgnoreCase("-tunnel")) {
				String value = args.poll();
				String[] split = value.split("\\:");
				tunnelHost = split[0];
				tunnelPort = Integer.parseInt(split[1]);
			} else if(option.equalsIgnoreCase("-server")) {
				tunnelPort = Integer.parseInt(args.poll());
				mode = Mode.TunnelServer;
			} else {
				throw new RuntimeException("Unrecognized option \"" + option + "\", see -help for info.");
			}
		}

		if(mode != null) {
			switch (mode) {
				case LoopbackServer:
					if(listenPort != null && tunnelHost != null && tunnelPort != null && sessionId != null) {
						MultiLog.v("AdeHttpTunnel", "Starting loopback forwarder at local port " + listenPort + " forwarding to meeting point " + tunnelHost + ":" + tunnelPort + " for session id: " + sessionId);

						new MeetingPointLoopbackServer(listenPort, tunnelHost, tunnelPort, sessionId).start();
					}
					break;
				case ConsoleSocket:
					MultiLog.v("AdeHttpTunnel", "Opening a test connection to " + host + ":" + port + "...");
					new ConsoleSocket(host, port).start();
					break;
				case HelloWorldServer:
					MultiLog.v("AdeHttpTunnel", "Starting a hello world server on port " + listenPort + ".");
					new HelloWorldServer(listenPort).start();
					break;
				case RemoteService:
					MultiLog.v("AdeHttpTunnel", "Opening a remote service connection to " + host + ":" + port + " tunneled to " + tunnelHost + ":" + tunnelPort + " with session id: " + sessionId);
					new MeetingPointToRemoteServiceClient(host, port, tunnelHost, tunnelPort, sessionId).start();
					break;
				case TunnelServer:
					MultiLog.v("AdeHttpTunnel", "Starting a tunnel server on port " + tunnelPort);
					new FakeHttpServer(tunnelPort).start();
					break;
			}
		}

	}
}
