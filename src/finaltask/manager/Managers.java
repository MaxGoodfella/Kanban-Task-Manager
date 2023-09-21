package finaltask.manager;

import java.io.File;

public final class Managers {

    private Managers() {}

    private static final HistoryManager defaultHistoryManager = new InMemoryHistoryManager();
    private static final TaskManager defaultTaskManager = new InMemoryTaskManager(defaultHistoryManager);

    private static final FileBackedTaskManager defaultFileBackedTaskManager = new FileBackedTaskManager(new File("./resources/sprint6/test.txt"), defaultHistoryManager);


    public static TaskManager getDefault() {
        return defaultTaskManager;
    }

    public static HistoryManager getHistoryDefault() {
        return defaultHistoryManager;
    }

}