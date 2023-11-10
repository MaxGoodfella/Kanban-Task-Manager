package finaltask.manager;

import java.io.File;

public final class Managers {

    private Managers() {}

    private static final HistoryManager defaultHistoryManager = new InMemoryHistoryManager();
    private static final InMemoryTaskManager defaultTaskManager = new InMemoryTaskManager(defaultHistoryManager);
    private static final FileBackedTaskManager defaultFileBackedTaskManager = new FileBackedTaskManager(new File("/Users/MaximGuseynov/dev3/sprint8/java-kanban/src/resources/sprint7/test.txt"), defaultHistoryManager);

    private static final HTTPTaskManager defaultHttpTaskManager = new HTTPTaskManager("http://localhost:8078/");



    public static HTTPTaskManager getDefault() {
        // return defaultTaskManager;
        //return new HTTPTaskManager("http://localhost:8078/");
        return defaultHttpTaskManager;
    }

    public static HistoryManager getHistoryDefault() {
        return defaultHistoryManager;
    }

    public static FileBackedTaskManager getDefaultFileBackedTaskManager() {
        return defaultFileBackedTaskManager;
    }

}