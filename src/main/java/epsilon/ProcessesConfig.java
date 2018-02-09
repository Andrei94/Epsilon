package epsilon;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("UtilityClass")
@Component
public class ProcessesConfig {
	private final Map<String, List<String>> map = new HashMap<String, List<String>>() {{
		put("CertMS", argsList("D:\\Programming\\Master\\CertMS\\CertMS\\bin\\Debug\\CertMS.exe"));
		put("CertMSCRUD", argsList("D:\\Programming\\Master\\CertMSCRUD\\CertMSCRUD\\bin\\Debug\\CertMSCRUD.exe"));
		put("CertMSGame", argsList("D:\\Programming\\Master\\CertMSGame\\CertMSGame\\bin\\Debug\\CertMSGame.exe"));
		put("CertMSGA", argsList("D:\\Programming\\Master\\CertMSGA\\CertMSGA\\bin\\Debug\\CertMSGA.exe", "D:\\Programming\\Master\\CertMSGA\\CertMSGA\\bin\\Debug\\asdfghjkl.kpp"));
		put("CertMSSearch", argsList("D:\\Programming\\Master\\CertMSSearch\\CertMSSearch\\bin\\Debug\\CertMSSearch.exe"));
	}};

	private List<String> argsList(final String... args) {
		return Arrays.asList(args);
	}

	public Optional<String> getExecutable(final String key) {
		final String[] x = {null};
		Optional.ofNullable(map.get(key)).ifPresent(args -> x[0] = args.iterator().next());
		return Optional.ofNullable(x[0]);
	}

	public List<String> getAdditionalFiles(final String key) {
		return map.get(key).stream().skip(1).collect(Collectors.toList());
	}
}
