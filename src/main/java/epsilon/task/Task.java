package epsilon.task;

import helpers.Constants;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Task {
	private final Process process;
	private final long startTime;
	private final Path workingDirectory;

	public Task(final Process process, final Path workingDirectory) {
		this.process = process;
		this.workingDirectory = workingDirectory;
		this.startTime = System.currentTimeMillis();
	}

	public Task(final Process process, final String workingDirectory) {
		this(process, Paths.get(workingDirectory));
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

	public Path workingDirectory() {
		return workingDirectory;
	}
}
