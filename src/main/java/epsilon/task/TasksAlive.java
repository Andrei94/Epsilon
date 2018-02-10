package epsilon.task;

import epsilon.TaskSupplier;
import helpers.EpsilonLogger;

public class TasksAlive implements TaskOps {
	private final EpsilonLogger logger = new EpsilonLogger(TasksAlive.class);

	@Override
	public void execute(final TaskSupplier tasks) {
		tasks.forEach((name, task) -> {
			if(!task.isAlive()) {
				tasks.removeTask(name);
				logger.info("Removed stale task");
			}
		});
	}
}
