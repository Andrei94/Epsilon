package epsilon;

import epsilon.task.TasksAlive;
import epsilon.task.TasksOvertime;
import helpers.EpsilonLogger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;

import static helpers.Utils.getReturnFromLambda;

@RestController
public class ProcessController {
	private final EpsilonLogger logger = new EpsilonLogger(ProcessController.class);

	private final ProcessesConfig config;
	private final RoutingController routingController;

	public ProcessController(final ProcessesConfig config, final RoutingController routingController) {
		this.config = config;
		this.routingController = routingController;
	}

	@PostConstruct
	void startWatcher() {
		routingController.setTaskWatcherOps(Arrays.asList(new TasksAlive(), new TasksOvertime(1)));
		routingController.startWatcher();
	}

	@PostMapping("/start/{name}")
	public String startProcess(@PathVariable("name") final String name,
	                           @RequestParam final Map<String, String> body) {
		return getReturnFromLambda(response ->
				logger.runningTime(name, body.entrySet().toString(),
						() -> {
							if(config.getExecutable(name).isPresent())
								response.append(routingController.startProcess(name, new Arguments(config.getExecutable(name).get(), body.values(), config.getAdditionalFiles(name))));
							else
								logger.info("The name " + name + " was not found in the configuration");
						}));
	}

	@PostMapping("/kill/{name}")
	public void killProcess(@PathVariable("name") final String name) {
		routingController.getProcess(name).ifPresent(process -> routingController.killProcess(name, process));
	}
}
