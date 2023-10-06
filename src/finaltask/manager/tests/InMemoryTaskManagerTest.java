package finaltask.manager.tests;


import finaltask.manager.InMemoryTaskManager;
import finaltask.manager.Managers;
import finaltask.tasks.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    // здесь для этого класса


    @BeforeEach
    void setUpFirst() {
        taskManager = new InMemoryTaskManager(Managers.getHistoryDefault());
        init();
    }

    @Test
    public void createEpic() {
        Epic epicCreated = taskManager.createEpic(epic);
        // assertNotNull(epicCreated);
        // assertTrue(epicCreated.getAllSubtaskIDs().isEmpty());
        assertNull(epicCreated);
    }




}
