package se.ade.httptunnel.fakehttp.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpSession {
	private String sessionId;
	public HashMap<String, Puller> pullers = new HashMap<String, Puller>();
	public HashMap<String, Pusher> pushers = new HashMap<String, Pusher>();

	public HttpSession(String sessionId) {
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
