package se.ade.httptunnel.client;

import se.ade.httptunnel.InfiniteStream;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeetingPointSocket {
	ExecutorService executorService = Executors.newFixedThreadPool(2);
	MeetingPointSocketReader reader;
	MeetingPointSocketWriter writer;

	private InfiniteStream inputStream = new InfiniteStream();
	private InfiniteStream outputStream = new InfiniteStream();

	private String host;
	private int port;
	private String sessionId;
	private ProxyConfiguration proxyConfiguration;
	private String clientId;

	private boolean connected = true;

	public MeetingPointSocket(String host, int port, String sessionId, ProxyConfiguration proxyConfiguration) {
		this.host = host;
		this.port = port;
		this.sessionId = sessionId;
		this.proxyConfiguration = proxyConfiguration;

		reader = new MeetingPointSocketReader(this);
		writer = new MeetingPointSocketWriter(this);

		clientId = (Math.random() + "").replace(".", "").replace(",", "");
	}

	public void connect() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				reader.start(host, port, sessionId, proxyConfiguration, clientId);
			}
		});

		executorService.execute(new Runnable() {
			@Override
			public void run() {
				writer.start(host, port, sessionId, proxyConfiguration, clientId);
			}
		});

		while(connected) {
			if(!reader.isOpen() || !writer.isOpen()) {
				connected = false;
			}

			if(reader.hasData()) {
				inputStream.write(reader.getData());
			}

			while(outputStream.available() > 0) {
				writer.send(outputStream.readAll());
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		System.out.println("MeetingPointSocket closed");
	}

	public InfiniteStream getInputStream() {
		return inputStream;
	}

	public InfiniteStream getOutputStream() {
		return outputStream;
	}

	public boolean isConnected() {
		return connected;
	}
}
