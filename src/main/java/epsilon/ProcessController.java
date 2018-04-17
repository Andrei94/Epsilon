package epsilon;

import epsilon.task.TasksAlive;
import epsilon.task.TasksOvertime;
import helpers.EpsilonLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;

import static helpers.Utils.getReturnFromLambda;

@RestController
@EnableAutoConfiguration
@Import({ProcessesConfig.class, RoutingController.class})
public class ProcessController {
	private final EpsilonLogger logger = new EpsilonLogger(ProcessController.class);

	private final ProcessesConfig config;
	private final RoutingController routingController;

	@Autowired
	public ProcessController(final ProcessesConfig config, final RoutingController routingController) {
		this.config = config;
		this.routingController = routingController;
	}

	@PostConstruct
	void startWatcher() {
		routingController.setTaskWatcherOps(Arrays.asList(new TasksAlive(), new TasksOvertime(1)));
		routingController.startWatcher();
	}

	@RequestMapping("/start/{name}")
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

	@RequestMapping("/kill/{name}")
	public void killProcess(@PathVariable("name") final String name) {
		routingController.getProcess(name).ifPresent(process -> routingController.killProcess(name, process));
	}
}
