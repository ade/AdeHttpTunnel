package se.ade.httptunnel.client;

public class ProxyConfiguration {
	public String host;
	public int port;

	public ProxyConfiguration(String host, int port) {
		this.host = host;
		this.port = port;
	}
}
