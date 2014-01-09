package se.ade.httptunnel.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: adrnil
 * Date: 2014-01-09
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
public class Session {
	private String sessionId;
	public HashMap<String, Puller> pullers = new HashMap<String, Puller>();
	public HashMap<String, Pusher> pushers = new HashMap<String, Pusher>();

	public Session(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void onData(byte[] data, String pusherClientId) throws IOException {
		for(Map.Entry<String, Puller> entry : pullers.entrySet()) {
			String clientId = entry.getKey();
			Puller puller = entry.getValue();

			if(!pusherClientId.equals(clientId)) {
				puller.onBrokeredData(data);
			}
		}
	}
}
