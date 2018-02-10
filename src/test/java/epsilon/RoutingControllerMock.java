package epsilon;

import epsilon.task.ProcessCreator;
import epsilon.task.Task;
import helpers.Constants;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RoutingControllerMock extends RoutingController {
	private boolean watcherRunning = false;
	private Path tempDirectory;

	@Override
	String startProcess(final String programKey, final Arguments args) {
		assertEquals(0, processes.size());
		super.startProcess(programKey, args);
		assertEquals(tempDirectory, processes.get(programKey).workingDirectory());
		assertEquals(1, processes.size());
		return Constants.EMPTY_STRING;
	}

	@Override
	Task startTask(final Arguments args) {
		return new Task(ProcessCreator.createAliveProcess(), tempDirectory);
	}

	@Override
	Path createTempDirectory() {
		tempDirectory = Paths.get("temp");
		return tempDirectory;
	}

	@Override
	void deleteWorkingDirectory(final Task process) {
		tempDirectory = null;
	}

	@Override
	String getProcessResponse(final String proc) {
		final Task task = processes.get(proc);
		deleteWorkingDirectory(task);
		assertNull(tempDirectory);
		return "It works!";
	}

	@Override
	void startWatcher() {
		super.startWatcher();
		watcherRunning = true;
	}

	boolean isWatcherRunning() {
		return watcherRunning;
	}
}
