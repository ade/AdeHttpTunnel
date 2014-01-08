package se.ade.httptunnel.client;

/**
 * Created with IntelliJ IDEA.
 * User: adrnil
 * Date: 2014-01-08
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleClient {
	public static void main(String[] args) {
		new MeetingPointSocket("127.0.0.1", 8080, "foo");
	}
}
