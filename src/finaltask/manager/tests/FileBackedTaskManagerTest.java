package finaltask.manager.tests;

import finaltask.manager.FileBackedTaskManager;
import finaltask.manager.InMemoryHistoryManager;

import java.io.File;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    protected FileBackedTaskManager createTaskManager() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        // File file = new File("/Users/MaximGuseynov/dev3/sprint7/java-kanban/src/resources/sprint7/test.txt");
        File file = new File("java-kanban/src/resources/sprint7/test.txt"); // сделал вот так, "относительнее" не запускает
        return new FileBackedTaskManager(file, historyManager);
    }
}