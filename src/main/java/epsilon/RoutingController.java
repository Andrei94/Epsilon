package epsilon;

import epsilon.task.Task;
import epsilon.task.TaskOps;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class RoutingController {
	final Map<String, Task> processes = new ConcurrentHashMap<>();
	private final Watcher taskWatcher = new Watcher(processes);

	String startProcess(final String programKey, final List<String> args) {
		try {
			processes.put(programKey, new Task(startProcess(args)));
			return getProcessResponse(programKey);
		} catch(final IOException e) {
			throw new RuntimeException(e);
		}
	}

	Process startProcess(final List<String> args) throws IOException {
		return new ProcessBuilder(args).start();
	}

	String getProcessResponse(final String proc) {
		try {
			final StringBuilder response = new StringBuilder();
			executeInSeparateThread(() -> Optional.ofNullable(processes.get(proc)).ifPresent(task -> response.append(task.getContent())));
			return response.toString();
		} catch(final InterruptedException e) {
			return Constants.EMPTY_STRING;
		}
	}

	private void executeInSeparateThread(final Runnable toExecute) throws InterruptedException {
		final Thread reader = new Thread(toExecute);
		reader.start();
		reader.join();
	}

	public Optional<Task> getProcess(final String name) {
		return Optional.ofNullable(processes.get(name));
	}

	void killProcess(final String name, final Task process) {
		process.kill();
		processes.remove(name);
	}

	void startWatcher() {
		taskWatcher.startWatcher();
	}

	void setTaskWatcherOps(final List<TaskOps> ops) {
		taskWatcher.setOps(ops);
	}
}
