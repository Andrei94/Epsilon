package Epsilon.Epsilon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EpsilonApplicationTests {
	private RoutingController controller;
	private final ProcessesConfig config = new ProcessesConfig();

	@Before
	public void setUp() {
		controller = new RoutingControllerMock();
	}

	@Test
	public void processStartsAndEnds() {
		final String proc = "CertMS";
		config.get(proc).ifPresent(path -> controller.startProcess(proc, Arrays.asList(path, Constants.EMPTY_STRING, Constants.EMPTY_STRING)));
		controller.getProcess(proc).ifPresent(p -> controller.killProcess(proc, p));
	}

	@Test
	public void startProcessWithOneArgument() {
		startProcess("save_duplicate", Constants.EMPTY_STRING);
	}

	@Test
	public void startProcessWithTwoArguments() {
		startProcess("delete", "1234");
	}

	private void startProcess(final String op, final String data) {
		final String proc = "CertMSCRUD";
		config.get(proc).ifPresent(path -> controller.startProcess(proc, Arrays.asList(path, op, data)));
	}

	@After
	public void clean() {
		controller.getProcess("CertMS").ifPresent(p -> controller.killProcess("CertMS", p));
		controller.getProcess("CertMSCRUD").ifPresent(p -> controller.killProcess("CertMSCRUD", p));
	}

	private class RoutingControllerMock extends RoutingController {
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
	}
}
