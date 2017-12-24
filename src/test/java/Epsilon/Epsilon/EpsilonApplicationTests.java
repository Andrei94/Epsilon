package Epsilon.Epsilon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class EpsilonApplicationTests {
	private RoutingController controller;
	private final ProcessesConfig config = new ProcessesConfig();

	@Before
	public void setUp() {
		controller = new RoutingController();
	}

	@Test
	public void processStartsAndEnds() {
		final String proc = "CertMS";
		config.get(proc).ifPresent(path -> controller.startProcess(proc, Arrays.asList(path, Constants.EMPTY_STRING, Constants.EMPTY_STRING)));
		assertEquals(1, controller.processCount());
		controller.getProcess(proc).ifPresent(p -> controller.killProcess(proc, p));
		assertEquals(0, controller.processCount());
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
		assertEquals(1, controller.processCount());
		assertFalse(controller.getProcessResponse(proc).isEmpty());
		assertEquals(0, controller.processCount());
	}

	@After
	public void clean() {
		controller.getProcess("CertMS").ifPresent(p -> controller.killProcess("CertMS", p));
		controller.getProcess("CertMSCRUD").ifPresent(p -> controller.killProcess("CertMSCRUD", p));
	}
}
