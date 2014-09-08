package se.ade.httptunnel;

import java.io.*;

public class InfiniteStream extends InputStream {
	public ByteArrayOutputStream inputBuffer = new ByteArrayOutputStream();
	public ByteArrayInputStream output;

	@Override
	public int available() {
		return inputBuffer.size();
	}

	@Override
	public int read() throws IOException {
		if(output != null && output.available() > 0) {
			return output.read();
		}

		while(inputBuffer.size() == 0) {
			//Block caller
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		output.read();

		output = new ByteArrayInputStream(readAll());
	    return output.read();
	}

	public synchronized byte[] read(int maxLength) {
		try {
			if(inputBuffer.size() > maxLength) {
				byte[] allData = inputBuffer.toByteArray();
				byte[] returnData = new byte[maxLength];
				byte[] newBuffer = new byte[allData.length - maxLength];
				for(int i = 0; i < maxLength; i++) {
					returnData[i] = allData[i];
				}

				for(int i = maxLength; i < allData.length; i++) {
					newBuffer[i] = allData[i];
				}
				inputBuffer.reset();
				inputBuffer.write(newBuffer);
				output = new ByteArrayInputStream(newBuffer);
				return returnData;
			} else {
				byte[] allData = inputBuffer.toByteArray();
				inputBuffer.reset();
				return allData;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized void write(byte[] data) {
		try {
			inputBuffer.write(data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized byte[] readAll() {
		byte[] ret = inputBuffer.toByteArray();
		inputBuffer.reset();
		return ret;
	}

	public synchronized void consumeAvailable(InputStream inputStream) throws IOException {
		int dataLengthToSend;
		while((dataLengthToSend = inputStream.available()) > 0) {
			byte[] data = new byte[dataLengthToSend];
			int gotBytes = inputStream.read(data);

			if(gotBytes != dataLengthToSend) {
				throw new RuntimeException("Not implemented");
			}

			write(data);
		}

	}
}
