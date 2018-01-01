package epsilon.task;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BaseProcess extends Process {
	private boolean isAlive;

	BaseProcess(final boolean isAlive) {
		this.isAlive = isAlive;
	}

	@Override
	public OutputStream getOutputStream() {
		return null;
	}

	@Override
	public InputStream getInputStream() {
		return isAlive ? new ByteArrayInputStream("It works!".getBytes()) : new InputStream() {
			@Override
			public int read() throws IOException {
				throw new IOException();
			}
		};
	}

	@Override
	public InputStream getErrorStream() {
		return null;
	}

	@Override
	public int waitFor() {
		return 0;
	}

	@Override
	public int exitValue() {
		if(isAlive)
			throw new IllegalThreadStateException();
		return -1;
	}

	@Override
	public void destroy() {
		isAlive = false;
	}
}
