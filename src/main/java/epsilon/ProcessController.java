package epsilon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import task.TasksAlive;
import task.TasksOvertime;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@EnableAutoConfiguration
@Import({ProcessesConfig.class, RoutingController.class})
public class ProcessController {
	private final Logger logger = Logger.getLogger(ProcessController.class.getName());

	@Autowired
	private ProcessesConfig config;
	@Autowired
	private RoutingController routingController;

	@PostConstruct
	void startWatcher() {
		routingController.setTaskWatcherOps(Arrays.asList(new TasksAlive(), new TasksOvertime(2)));
		routingController.startWatcher();
	}

	@RequestMapping("/start/{name}")
	public String startProcess(@PathVariable("name") final String name,
	                           @RequestParam final Map<String, String> body) {
		logger.info("Process " + name + " started with " + body.entrySet());
		final StringBuilder response = new StringBuilder();
		config.get(name).ifPresent(path -> response.append(routingController.startProcess(name, Stream.concat(Stream.of(path), body.values().stream())
				.collect(Collectors.toList()))));
		return response.toString();
	}

	@RequestMapping("/kill/{name}")
	public void killProcess(@PathVariable("name") final String name) {
		routingController.getProcess(name).ifPresent(process -> routingController.killProcess(name, process));
	}
}
