package finaltask.manager.tests;

import finaltask.manager.FileBackedTaskManager;
import finaltask.manager.InMemoryHistoryManager;

import java.io.File;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    protected FileBackedTaskManager createTaskManager() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        File file = new File("java-kanban/src/resources/sprint7/test.txt");
        return new FileBackedTaskManager(file, historyManager);
    }
}