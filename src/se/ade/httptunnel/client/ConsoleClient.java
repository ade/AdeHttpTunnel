package se.ade.httptunnel.client;

public class ConsoleClient {
	public static void main(String[] args) {
		new MeetingPointSocket("127.0.0.1", 8080, "foo");
	}
}
