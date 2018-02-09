package epsilon;

import epsilon.task.Task;
import epsilon.task.TaskOps;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
		args.additionalFiles().forEach(fileToCopy -> copyFileToDirectory(fileToCopy, workingDirectory));
		return new Task(new ProcessBuilder(args.args()).directory(workingDirectory.toFile()).start(), workingDirectory);
	}

	Path createTempDirectory() throws IOException {
		return Files.createTempDirectory("epsilon");
	}

	private void copyFileToDirectory(final String srcFile, final Path directory) {
		try {
			FileUtils.copyFileToDirectory(new File(srcFile), directory.toFile());
		} catch(final IOException e) {
			e.printStackTrace();
		}
	}

	String getProcessResponse(final String proc) {
		try {
			final StringBuilder response = new StringBuilder();
			executeInSeparateThread(() -> Optional.ofNullable(processes.get(proc)).ifPresent(task -> {
				response.append(task.getContent());
				deleteWorkingDirectory(task);
			}));
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
		deleteWorkingDirectory(process);
		processes.remove(name);
	}

	void deleteWorkingDirectory(final Task process) {
		try {
			FileUtils.deleteDirectory(process.workingDirectory().toFile());
		} catch(final IOException e) {
			e.printStackTrace();
		}
	}

	void startWatcher() {
		taskWatcher.startWatcher();
	}

	void setTaskWatcherOps(final List<TaskOps> ops) {
		taskWatcher.setOps(ops);
	}
}
