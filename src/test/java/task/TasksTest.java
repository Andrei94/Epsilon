package task;

import epsilon.TaskSupplier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static task.ProcessCreator.createAliveProcess;
import static task.ProcessCreator.createDeadProcess;

class TasksTest {
	private TaskSupplier supplier;

	@Nested
	class AliveTasks {
		@Test
		void executeAliveTask() {
			supplier = createOneSizeTaskSupplier(createAliveProcess());
			execute();
			aliveTasksAre(1);
		}

		@Test
		void executeDeadTask() {
			supplier = createOneSizeTaskSupplier(createDeadProcess());
			execute();
			aliveTasksAre(0);
		}

		private void execute() {
			new TasksAlive().execute(supplier);
		}

		private void aliveTasksAre(final int expectedNumber) {
			assertEquals(expectedNumber, supplier.size());
		}
	}

	@Nested
	class OvertimeTasks {
		@Test
		void executeInTimeTask() {
			supplier = createOneSizeTaskSupplier(createAliveProcess());
			execute(2);
			overtimeTasksAre(1);
		}

		@Test
		void executeOvertimeTask() {
			supplier = createOneSizeTaskSupplier(createDeadProcess());
			execute(0);
			overtimeTasksAre(0);
		}

		private void execute(final int limit) {
			new TasksOvertime(limit).execute(supplier);
		}

		private void overtimeTasksAre(final int expectedNumber) {
			assertEquals(expectedNumber, supplier.size());
		}
	}

	private TaskSupplier createOneSizeTaskSupplier(final BaseProcess process) {
		return new TaskSupplier(Collections.singletonMap("test", new Task(process)));
	}
}