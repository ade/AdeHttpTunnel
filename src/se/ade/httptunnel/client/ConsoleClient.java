package se.ade.httptunnel.client;

import se.ade.httptunnel.Config;

public class ConsoleClient {
	public static void main(String[] args) {
		new MeetingPointSocket(Config.SERVER_HOST, Config.SERVER_PORT, "foo", new ProxyConfiguration("secproxy1.sec.intra", 8181));
	}
}
