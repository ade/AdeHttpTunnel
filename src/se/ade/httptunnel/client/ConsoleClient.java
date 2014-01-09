package se.ade.httptunnel.client;

import se.ade.httptunnel.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsoleClient {
	ExecutorService executor = Executors.newSingleThreadExecutor();

	public ConsoleClient() {
		final MeetingPointSocket socket = new MeetingPointSocket(Config.SERVER_HOST, Config.SERVER_PORT, "foo", new ProxyConfiguration("secproxy1.sec.intra", 8181));
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
				System.out.println("Have some data: '" + new String(data) + "'");
				socket.getOutputStream().write(input.getBytes());
			}
		}
	}

	public static void main(String[] args) {
		new ConsoleClient();

	}
}
