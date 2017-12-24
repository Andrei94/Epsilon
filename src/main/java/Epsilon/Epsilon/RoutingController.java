package Epsilon.Epsilon;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@EnableAutoConfiguration
public class RoutingController {
    private final Map<String, Process> processes = new ConcurrentHashMap<>();

	public RoutingController() {
		final Thread procWatcher = new Thread(() -> {
			//noinspection InfiniteLoopStatement
			while(true) {
				processes.forEach((key, process) -> {
					if(!process.isAlive())
						processes.remove(key);
				});
			}
		});
		procWatcher.setDaemon(true);
		procWatcher.start();
	}

	void startProcess(final String programKey, final List<String> args) {
		try {
			processes.put(programKey, new ProcessBuilder(args).start());
		} catch(final IOException e) {
			throw new RuntimeException(e);
		}
	}

	void killProcess(final String name, final Process process) {
		process.destroy();
		processes.remove(name);
	}

	int processCount() {
		return processes.size();
	}

	String getProcessResponse(final String proc) {
		final StringBuilder response = new StringBuilder();
		final Thread reader = new Thread(() -> {
			try {
				response.append(IOUtils.toString(processes.get(proc).getInputStream()));
			} catch(final IOException ignored) {
			}
		});
		reader.start();
		try {
			reader.join();
		} catch(final InterruptedException ignored) {
		}
		return response.toString().trim();
	}

	public Optional<Process> getProcess(final String name) {
		return Optional.ofNullable(processes.get(name));
	}
}
