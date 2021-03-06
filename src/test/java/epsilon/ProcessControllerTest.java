package epsilon;

import epsilon.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProcessControllerTest {
	private final String certMS = "CertMS";
	@Autowired
	private ProcessController controller;

	@Test
	void startProcess() {
		ReflectionTestUtils.setField(controller, "routingController", new RoutingControllerMock());
		assertEquals("It works!", controller.startProcess(certMS, Collections.emptyMap()));
	}

	@AfterEach
	void cleanup() {
		controller.killProcess(certMS);
	}

	private class RoutingControllerMock extends RoutingController {
		@Override
		String startProcess(final String programKey, final Arguments args) {
			return getProcessResponse(programKey);
		}

		@Override
		void killProcess(final String name, final Task process) {
			assertEquals(1, processes.size());
			super.killProcess(name, process);
			assertEquals(0, processes.size());
		}

		@Override
		String getProcessResponse(final String proc) {
			return "It works!";
		}
	}
}
