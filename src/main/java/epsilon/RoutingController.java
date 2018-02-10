package epsilon;

import epsilon.task.Task;
import epsilon.task.TaskOps;
import helpers.Constants;
import helpers.FileHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static helpers.Utils.executeInSeparateThread;
import static helpers.Utils.getReturnFromLambda;

public class RoutingController {
	final ConcurrentMap<String, Task> processes = new ConcurrentHashMap<>();
	private final Watcher taskWatcher = new Watcher(processes);

	String startProcess(final String programKey, final Arguments args) {
		try {
			processes.put(programKey, startTask(args));
			return getProcessResponse(programKey);
		} catch(final IOException e) {
			throw new RuntimeException(e);
		}
	}

	Task startTask(final Arguments args) throws IOException {
		final Path workingDirectory = createTempDirectory();
		copyFilesToDirectory(workingDirectory, args.additionalFiles());
		return new Task(new ProcessBuilder(args.args()).directory(workingDirectory.toFile()).start(), workingDirectory);
	}

	Path createTempDirectory() {
		return FileHelper.createTempDirectory();
	}

	private void copyFilesToDirectory(final Path directory, final Iterable<String> files) {
		FileHelper.copyFilesToDirectory(directory, files);
	}

	String getProcessResponse(final String proc) {
		return getReturnFromLambda(response -> {
			try {
				executeInSeparateThread(() -> Optional.ofNullable(processes.get(proc)).ifPresent(task -> {
					response.append(task.getContent());
					deleteWorkingDirectory(task);
				}));
			} catch(final InterruptedException e) {
				response.append(Constants.EMPTY_STRING);
			}
		});
	}

	public Optional<Task> getProcess(final String name) {
		return Optional.ofNullable(processes.get(name));
	}

	void killProcess(final String name, final Task process) {
		process.kill();
		deleteWorkingDirectory(process);
		processes.remove(name);
	}

	void deleteWorkingDirectory(final Task process) {
		FileHelper.deleteDirectory(process.workingDirectory());
	}

	void startWatcher() {
		taskWatcher.startWatcher();
	}

	void setTaskWatcherOps(final List<TaskOps> ops) {
		taskWatcher.setOps(ops);
	}
}
