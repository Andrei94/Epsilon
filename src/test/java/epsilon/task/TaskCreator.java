package epsilon.task;

import static epsilon.task.ProcessCreator.createAliveProcess;
import static epsilon.task.ProcessCreator.createDeadProcess;

@SuppressWarnings("UtilityClass")
final class TaskCreator {
	static Task createAliveTask() {
		return new Task(createAliveProcess());
	}

	static Task createDeadTask() {
		return new Task(createDeadProcess());
	}
}
