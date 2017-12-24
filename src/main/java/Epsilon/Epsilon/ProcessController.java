package Epsilon.Epsilon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.logging.Logger;

@RestController
@EnableAutoConfiguration
@Import({ProcessesConfig.class, RoutingController.class})
public class ProcessController {
	private final Logger logger = Logger.getLogger(ProcessController.class.getName());
	@Autowired
	private ProcessesConfig config;

	private final RoutingController routingController = new RoutingController();

	public ProcessController() {
		routingController.startWatcher();
	}

	@RequestMapping("/start/{name}")
	public String startProcess(@PathVariable("name") final String name,
	                           @RequestParam("op") final String operation,
	                           @RequestParam("data") final String data) {
		logger.info("Process " + name + " started with " + operation + " and " + data);
		final StringBuilder response = new StringBuilder();
		config.get(name).ifPresent(path -> response.append(routingController.startProcess(name, Arrays.asList(path, operation, data))));
		return response.toString();
	}

	@RequestMapping("/kill/{name}")
	public void killProcess(@PathVariable("name") final String name) {
		routingController.getProcess(name).ifPresent(process -> routingController.killProcess(name, process));
	}
}
