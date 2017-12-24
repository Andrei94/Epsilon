package Epsilon.Epsilon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings({"UtilityClass", "NonFinalUtilityClass"})
@SpringBootApplication
public class EpsilonApplication {
	public static void main(final String... args) {
		SpringApplication.run(ProcessController.class, args);
	}
}
