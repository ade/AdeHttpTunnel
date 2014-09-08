package se.ade.httptunnel;

import se.ade.httptunnel.fakehttp.client.MeetingPointSocket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsoleClient {
	ExecutorService executor = Executors.newSingleThreadExecutor();

	public ConsoleClient() {
		final MeetingPointSocket socket = new MeetingPointSocket(Config.SERVER_HOST, Config.SERVER_PORT, "foo", Config.PROXY_CONFIG);
		executor.execute(new Runnable() {
			@Override
			public void run() {
				socket.connect();
			}
		});

		String input;
		while(true) {
			input = System.console().readLine();
			if(input == null || input.equals("q")) {
				return;
			} else {
				socket.getOutputStream().write(input.getBytes());
			}
			if(socket.getInputStream().available() > 0) {
				byte[] data = socket.getInputStream().readAll();
				MultiLog.v("AdeHttpTunnel", "Have some data: '" + new String(data) + "'");
				socket.getOutputStream().write(input.getBytes());
			}
		}
	}

	public static void main(String[] args) {
		new ConsoleClient();

	}
}
