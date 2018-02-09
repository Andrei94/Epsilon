package epsilon;

import epsilon.task.TasksAlive;
import epsilon.task.TasksOvertime;
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
import java.util.logging.Logger;

@RestController
@EnableAutoConfiguration
@Import({ProcessesConfig.class, RoutingController.class})
public class ProcessController {
	private final Logger logger = Logger.getLogger(ProcessController.class.getName());

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
		final long startTime = System.currentTimeMillis();
		logger.info("Process " + name + " started with " + body.entrySet());
		final StringBuilder response = new StringBuilder();
		config.getExecutable(name).ifPresent(path -> response.append(routingController.startProcess(name, new Arguments(path, body.values(), config.getAdditionalFiles(name)))));
		logger.info("Process " + name + " ended after " + (System.currentTimeMillis() - startTime) + " ms");
		return response.toString();
	}

	@RequestMapping("/kill/{name}")
	public void killProcess(@PathVariable("name") final String name) {
		routingController.getProcess(name).ifPresent(process -> routingController.killProcess(name, process));
	}
}
