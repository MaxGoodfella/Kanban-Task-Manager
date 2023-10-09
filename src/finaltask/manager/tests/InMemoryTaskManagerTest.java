package finaltask.manager.tests;


import finaltask.manager.InMemoryTaskManager;
import finaltask.manager.Managers;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    protected InMemoryTaskManager createTaskManager() {
        return Managers.getDefault();
    }
}
