package epsilon;

import epsilon.task.Task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

public class TaskSupplier {
	private final ConcurrentMap<String, Task> tasks;

	public TaskSupplier(final Map<String, Task> tasks) {
		this.tasks = new ConcurrentHashMap<>(tasks);
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
