package finaltask.tests;


import finaltask.manager.InMemoryHistoryManager;
import finaltask.manager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    protected InMemoryTaskManager createTaskManager() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        return new InMemoryTaskManager(historyManager);
    }
}