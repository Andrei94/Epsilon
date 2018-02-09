package epsilon;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Arguments {
	private final Stream<String> args;
	private final List<String> additionalFiles;

	Arguments(final String path, final Collection<String> args, final List<String> additionalFiles) {
		this.args = Stream.concat(Stream.of(path), args.stream());
		this.additionalFiles = additionalFiles;
	}

	public List<String> additionalFiles() {
		return additionalFiles;
	}

	public List<String> args() {
		return args.collect(Collectors.toList());
	}
}
