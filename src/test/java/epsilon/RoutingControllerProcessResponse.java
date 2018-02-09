package epsilon;

import epsilon.task.BaseProcess;
import epsilon.task.Task;

class RoutingControllerProcessResponse extends RoutingController {
	private final BaseProcess process;

	RoutingControllerProcessResponse(final BaseProcess process) {
		this.process = process;
	}

	@Override
	String getProcessResponse(final String proc) {
		processes.put(proc, new Task(process, Constants.EMPTY_STRING));
		return super.getProcessResponse(proc);
	}
}
