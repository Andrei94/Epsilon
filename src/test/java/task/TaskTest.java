package task;

import epsilon.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static task.TaskCreator.createAliveTask;
import static task.TaskCreator.createDeadTask;

class TaskTest {
	@Test
	void overtimeTask() {
		assertTrue(createAliveTask().overtime( 0));
	}

	@Test
	void inTimeTask() {
		assertFalse(createAliveTask().overtime( 1));
	}

	@Test
	void aliveTask() {
		assertTrue(createAliveTask().isAlive());
	}

	@Test
	void taskDead() {
		assertFalse(createDeadTask().isAlive());
	}

	@Test
	void destroyAliveTask() {
		final Task task = createAliveTask();
		task.kill();
		assertFalse(task.isAlive());
	}

	@Test
	void destroyDeadTask() {
		final Task task = createDeadTask();
		task.kill();
		assertFalse(task.isAlive());
	}

	@Test
	void getTaskContentFromAliveProcess() {
		assertEquals("It works!", createAliveTask().getContent());
	}

	@Test
	void getTaskContentFromDeadProcess() {
		assertEquals(Constants.EMPTY_STRING, createDeadTask().getContent());
	}
}