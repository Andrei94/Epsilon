package epsilon.task;

import epsilon.TaskSupplier;
import helpers.EpsilonLogger;

public class TasksOvertime implements TaskOps {
	private final EpsilonLogger logger = new EpsilonLogger(TasksOvertime.class);

	private final int limit;

	public TasksOvertime(final int limitInMinutes) {
		this.limit = limitInMinutes;
	}

	@Override
	public void execute(final TaskSupplier tasks) {
		tasks.forEach((name, task) -> {
			if(task.overtime(limit)) {
				task.kill();
				tasks.removeTask(name);
				logger.info("Killed overtime task");
			}
		});
	}
}
