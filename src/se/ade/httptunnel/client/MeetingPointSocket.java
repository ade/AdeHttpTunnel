package se.ade.httptunnel.client;

import se.ade.httptunnel.Protocol;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeetingPointSocket {
	ExecutorService executorService = Executors.newFixedThreadPool(2);
	MeetingPointSocketReader reader;
	MeetingPointSocketWriter writer;

	public MeetingPointSocket(final String host, final int port, final String sessionId) {
		reader = new MeetingPointSocketReader(this);
		writer = new MeetingPointSocketWriter(this);

		executorService.execute(new Runnable() {
			@Override
			public void run() {
				reader.start(host, port, sessionId);
			}
		});

		executorService.execute(new Runnable() {
			@Override
			public void run() {
				writer.start(host, port, sessionId);
			}
		});
	}
}
