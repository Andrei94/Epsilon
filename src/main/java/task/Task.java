package task;

import epsilon.Constants;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Task {
	private final Process process;
	private final long startTime;

	public Task(final Process process) {
		this.process = process;
		this.startTime = System.currentTimeMillis();
	}

	public boolean overtime(final int limitInMinutes) {
		return getRunningTime() >= TimeUnit.MINUTES.toMillis(limitInMinutes);
	}

	private long getRunningTime() {
		return System.currentTimeMillis() - startTime;
	}

	public String getContent() {
		try {
			return IOUtils.toString(process.getInputStream()).trim();
		} catch(final IOException e) {
			return Constants.EMPTY_STRING;
		}
	}

	public boolean isAlive() {
		return process.isAlive();
	}

	public void kill() {
		process.destroyForcibly();
	}
}
