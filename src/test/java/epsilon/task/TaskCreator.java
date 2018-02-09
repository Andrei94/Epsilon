package epsilon.task;

import epsilon.Constants;

import static epsilon.task.ProcessCreator.createAliveProcess;
import static epsilon.task.ProcessCreator.createDeadProcess;

@SuppressWarnings("UtilityClass")
final class TaskCreator {
	static Task createAliveTask() {
		return new Task(createAliveProcess(), Constants.EMPTY_STRING);
	}

	static Task createDeadTask() {
		return new Task(createDeadProcess(), Constants.EMPTY_STRING);
	}
}
