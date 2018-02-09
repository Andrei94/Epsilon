package epsilon;

import epsilon.task.Task;
import epsilon.task.TaskOps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class Watcher {
	private final TaskSupplier taskSupplier;
	private List<TaskOps> ops = new ArrayList<>();

	Watcher(final ConcurrentMap<String, Task> processes) {
		taskSupplier = new TaskSupplier(processes);
	}

	public void startWatcher() {
		final Thread procWatcher = new Thread(() -> {
			//noinspection InfiniteLoopStatement
			while(true)
				ops.forEach(op -> op.execute(taskSupplier));
		});
		procWatcher.setDaemon(true);
		procWatcher.start();
	}

	public void setOps(final List<TaskOps> ops) {
		this.ops = ops;
	}
}
