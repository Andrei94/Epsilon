package epsilon;

import epsilon.task.Task;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

public class TaskSupplier {
	private final ConcurrentMap<String, Task> tasks;

	public TaskSupplier(final ConcurrentMap<String, Task> tasks) {
		this.tasks = tasks;
	}

	public void forEach(final BiConsumer<String, Task> action) {
		tasks.forEach(action);
	}

	public void removeTask(final String name) {
		tasks.remove(name);
	}

	public int size() {
		return tasks.size();
	}
}
