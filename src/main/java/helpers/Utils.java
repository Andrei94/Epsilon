package helpers;

import java.util.function.Consumer;

@SuppressWarnings("UtilityClass")
public final class Utils {
	public static String getReturnFromLambda(final Consumer<StringBuffer> acc) {
		final StringBuffer response = new StringBuffer();
		acc.accept(response);
		return response.toString();
	}

	public static void executeInSeparateThread(final Runnable toExecute) throws InterruptedException {
		final Thread reader = new Thread(toExecute);
		reader.start();
		reader.join();
	}
}
