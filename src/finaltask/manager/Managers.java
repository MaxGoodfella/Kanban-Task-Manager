package finaltask.manager;

public final class Managers {

    private Managers() {}

    private static final HistoryManager defaultHistoryManager = new InMemoryHistoryManager();
    private static final TaskManager defaultTaskManager = new InMemoryTaskManager(defaultHistoryManager);


    public static TaskManager getDefault() {
        return defaultTaskManager;
    }

    public static HistoryManager getHistoryDefault() {
        return defaultHistoryManager;
    }
}