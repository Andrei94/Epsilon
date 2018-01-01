package task;

@SuppressWarnings("UtilityClass")
public final class ProcessCreator {
	public static BaseProcess createAliveProcess() {
		return new AliveProcess();
	}

	public static BaseProcess createDeadProcess() {
		return new DeadProcess();
	}
}
