package task;

import epsilon.TaskSupplier;

public class TasksAlive implements TaskOps {
	@Override
	public void execute(final TaskSupplier tasks) {
		tasks.forEach((name, task) -> {
			if(!task.isAlive())
				tasks.removeTask(name);
		});
	}
}
