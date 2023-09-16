package finaltask.manager;

import java.io.File;

public final class Managers {

    private Managers() {}

    private static final HistoryManager defaultHistoryManager = new InMemoryHistoryManager();
    private static final TaskManager defaultTaskManager = new InMemoryTaskManager(defaultHistoryManager);

    // private static final FileBackedTaskManager defaultFileBackedTaskManager = new FileBackedTaskManager(defaultHistoryManager);

    private static final FileBackedTaskManager defaultFileBackedTaskManager = new FileBackedTaskManager(new File("test.txt"), defaultHistoryManager);
    // private static final FileBackedTaskManager defaultFileBackedTaskManager = new FileBackedTaskManager(new File("/Users/MaximGuseynov/dev3/sprint6/java-kanban/test.txt"), defaultHistoryManager);



    public static TaskManager getDefault() {
        return defaultTaskManager;
        // каким-то образом нужно сделать что-то подобное:
        // return new FileBackedTaskManager(historyManager);
    }

    public static HistoryManager getHistoryDefault() {
        return defaultHistoryManager;
    }

    public static FileBackedTaskManager getFileBackedDefault() {
        return defaultFileBackedTaskManager;
    }


}