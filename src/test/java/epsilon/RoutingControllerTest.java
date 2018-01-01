package epsilon;

import epsilon.task.BaseProcess;
import epsilon.task.ProcessCreator;
import epsilon.task.Task;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoutingControllerTest {
	private final ProcessesConfig config = new ProcessesConfig();
	private final String process = "CertMSCRUD";

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
			config.get(process).ifPresent(path -> controller.startProcess(process, Arrays.asList(path, op, data)));
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

		@AfterEach
		void clean() {
			controller.getProcess(process).ifPresent(p -> controller.killProcess(process, p));
		}

		private class RoutingControllerMock extends RoutingController {
			private boolean watcherRunning = false;

			@Override
			String startProcess(final String programKey, final List<String> args) {
				assertEquals(0, processes.size());
				super.startProcess(programKey, args);
				assertEquals(1, processes.size());
				return Constants.EMPTY_STRING;
			}

			@Override
			String getProcessResponse(final String proc) {
				return "It works!";
			}

			@Override
			void startWatcher() {
				super.startWatcher();
				watcherRunning = true;
			}

			boolean isWatcherRunning() {
				return watcherRunning;
			}
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
			assertThrows(RuntimeException.class, () -> controller.startProcess(process, Lists.emptyList()));
		}

		private class RoutingControllerThrowsExceptionForResponse extends RoutingController {
			@Override
			Process startProcess(final List<String> args) throws IOException {
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

		private class RoutingControllerProcessResponse extends RoutingController {
			private final BaseProcess process;

			RoutingControllerProcessResponse(final BaseProcess process) {
				this.process = process;
			}

			@Override
			String getProcessResponse(final String proc) {
				processes.put(proc, new Task(process));
				return super.getProcessResponse(proc);
			}
		}
	}
}
