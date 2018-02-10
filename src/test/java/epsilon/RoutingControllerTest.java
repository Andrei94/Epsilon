package epsilon;

import epsilon.task.ProcessCreator;
import epsilon.task.Task;
import helpers.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class RoutingControllerTest {
	private final ProcessesConfig config = new ProcessesConfig();
	private final String process = "CertMdssSCRUD";

	@Nested
	class StartProcessesSuccessfully {
		private RoutingControllerMock controller;

		@BeforeEach
		void setUp() {
			controller = new RoutingControllerMock();
		}

		@Test
		void processStartsAndEnds() {
			startProcess(Constants.EMPTY_STRING, Constants.EMPTY_STRING);
			controller.getProcess(process).ifPresent(p -> controller.killProcess(process, p));
		}

		@Test
		void startProcessWithOneArgument() {
			startProcess("save_duplicate", Constants.EMPTY_STRING);
		}

		@Test
		void startProcessWithTwoArguments() {
			startProcess("delete", "1234");
		}

		private void startProcess(final String op, final String data) {
			config.getExecutable(process).ifPresent(path -> controller.startProcess(process, new Arguments(path, Arrays.asList(op, data), Collections.emptyList())));
		}

		@Test
		void watcherIsRunning() {
			controller.startWatcher();
			assertTrue(controller.isWatcherRunning());
		}

		@Test
		void watcherNotRunning() {
			assertFalse(controller.isWatcherRunning());
		}

	}

	@Nested
	class FailToStartProcess {
		private RoutingController controller;

		@BeforeEach
		void setUp() {
			controller = new RoutingControllerThrowsExceptionForResponse();
		}

		@Test
		void throwExceptionOnStart() {
			assertThrows(RuntimeException.class, () -> controller.startProcess(process, new Arguments(Constants.EMPTY_STRING, Collections.emptyList(), Collections.emptyList())));
		}

		private class RoutingControllerThrowsExceptionForResponse extends RoutingController {
			@Override
			Task startTask(final Arguments args) throws IOException {
				throw new IOException();
			}
		}
	}

	@Nested
	class ProcessResponse {
		private RoutingController controller;

		@Test
		void readAliveProcessResponse() {
			controller = new RoutingControllerProcessResponse(ProcessCreator.createAliveProcess());
			assertEquals("It works!", controller.getProcessResponse(process));
		}

		@Test
		void readDeadProcessResponse() {
			controller = new RoutingControllerProcessResponse(ProcessCreator.createDeadProcess());
			assertEquals(Constants.EMPTY_STRING, controller.getProcessResponse(process));
		}

	}

}
