package ComputeEngine.ComputeEngine;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("UtilityClass")
@Component
public class ProcessesConfig {
	private final Map<String, String> map = new HashMap<String, String>() {{
		put("CertMS", "D:\\Programming\\Master\\CertMS\\CertMS\\bin\\Debug\\CertMS.exe");
		put("CertMSCRUD", "D:\\Programming\\Master\\CertMSCRUD\\CertMSCRUD\\bin\\Debug\\CertMSCRUD.exe");
	}};

	public Optional<String> get(final String key) {
		return Optional.ofNullable(map.get(key));
	}
}
