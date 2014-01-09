package se.ade.httptunnel.client;

/**
 * Created with IntelliJ IDEA.
 * User: adrnil
 * Date: 2014-01-09
 * Time: 10:14
 * To change this template use File | Settings | File Templates.
 */
public class ProxyConfiguration {
	public String host;
	public int port;

	public ProxyConfiguration(String host, int port) {
		this.host = host;
		this.port = port;
	}
}
