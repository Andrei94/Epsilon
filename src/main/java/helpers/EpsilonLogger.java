package helpers;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.logging.Logger;

public class EpsilonLogger {
	private final Logger logger;

	public EpsilonLogger(final Class<?> clazz) {
		logger = Logger.getLogger(clazz.getName());
	}

	public void exception(final String msg, final Throwable e) {
		logger.severe(msg);
		logger.severe(ExceptionUtils.getStackTrace(e));
	}

	public void runningTime(final String id, final String data, final Runnable c) {
		final long startTime = System.currentTimeMillis();
		info(id + " started with " + data);
		c.run();
		info(id + " ended after " + (System.currentTimeMillis() - startTime) + " ms");
	}

	public void info(final String msg) {
		logger.info(msg);
	}
}
