package helpers;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings("UtilityClass")
public final class FileHelper {
	private static final EpsilonLogger logger = new EpsilonLogger(FileHelper.class);

	public static Path createTempDirectory() {
		try {
			final Path tempDirectory = Files.createTempDirectory("epsilon");
			logger.info("Directory " + tempDirectory + " created");
			return tempDirectory;
		} catch(final IOException e) {
			logger.exception("Failed to create temp directory", e);
			throw new RuntimeException(e);
		}
	}

	public static void copyFilesToDirectory(final Path directory, final Iterable<String> files) {
		files.forEach(file -> copyFileToDirectory(directory, file));
	}

	private static void copyFileToDirectory(final Path directory, final String file) {
		try {
			FileUtils.copyFileToDirectory(new File(file), directory.toFile());
			logger.info("File " + file + " copied successfully to " + directory);
		} catch(final IOException e) {
			logger.exception("Failed to copy file " + file + " to directory " + directory, e);
		}
	}

	public static void deleteDirectory(final Path directory) {
		try {
			FileUtils.deleteDirectory(directory.toFile());
			logger.info("Directory " + directory + " deleted");
		} catch(final IOException e) {
			logger.exception("Failed to delete directory " + directory, e);
		}
	}
}
