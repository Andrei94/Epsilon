package Epsilon.Epsilon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@EnableAutoConfiguration
@Import({ProcessesConfig.class, RoutingController.class})
public class ProcessController {
	@Autowired
	private ProcessesConfig config;

	@Autowired
	private RoutingController routingController;

	@RequestMapping("/start/{name}")
	public void startProcess(@PathVariable("name") final String name,
	                         @RequestParam("op") final String operation,
	                         @RequestParam("data") final String data) {
		config.get(name).ifPresent(path -> routingController.startProcess(name, Arrays.asList(path, operation, data)));
	}

	@RequestMapping("/kill/{name}")
	public void killProcess(@PathVariable("name") final String name) {
		routingController.getProcess(name).ifPresent(process -> routingController.killProcess(name, process));
	}
}
