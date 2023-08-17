package finaltask.manager;

public final class Managers {

    private Managers() {}

    private static final TaskManager defaultTaskManager = new InMemoryTaskManager();

    public static TaskManager getDefault() {
        return defaultTaskManager;
    }

    public static HistoryManager getHistoryDefault() {
        return new InMemoryHistoryManager();
    }
}
