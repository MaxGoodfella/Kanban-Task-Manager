package finaltask.manager.tests;

import finaltask.manager.HistoryManager;
import finaltask.manager.Managers;
import finaltask.manager.TaskManager;
import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;
import finaltask.tasks.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private HistoryManager historyManager;
    private TaskManager taskManager;

    private Task task;
    private Epic epic;
    private Subtask subtask;


    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getHistoryDefault();

        task = new Task("Task 1", "Description 1");
        task = taskManager.createTask(task);

        epic = new Epic("Epic 1", "Description 1");
        epic = taskManager.createEpic(epic);

        subtask = new Subtask("Subtask 1", "Description 1", TaskStatus.NEW, epic.getId());
        subtask = taskManager.createSubtask(subtask);
    }


    @Test
    void testGetEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История - это null");
        assertTrue(history.isEmpty(), "История не пустая");
    }

    @Test
    void testGetHistoryWhenOneTaskAdded() {
        historyManager.addTask(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void testGetHistoryWithoutDuplicates() {

        historyManager.addTask(task);
        historyManager.addTask(task);

        historyManager.addTask(epic);
        historyManager.addTask(epic);


        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(2, history.size(), "История не соответствует размеру");

    }

    @Test
    void testGetHistoryWhenSmthRemoved() {

        historyManager.addTask(task);
        historyManager.addTask(task);

        historyManager.addTask(epic);
        historyManager.addTask(epic);

        historyManager.addTask(subtask);

        historyManager.remove(taskManager.removeEpicByID(epic.getId()));


        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(1, history.size(), "История не соответствует размеру");
    }

    @Test
    void testGetHistoryAndVerifyThatOrderIsCorrect() {
        historyManager.addTask(task);
        historyManager.addTask(epic);
        historyManager.addTask(subtask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(3, history.size(), "История не соответствует размеру");

        assertEquals(task, history.get(0), "Первая задача в истории не соответствует ожиданию");
        assertEquals(epic, history.get(1), "Вторая задача в истории не соответствует ожиданию");
        assertEquals(subtask, history.get(2), "Третья задача в истории не соответствует ожиданию");
    }

    @AfterEach
    void clean() {
        historyManager.remove(taskManager.removeTaskByID(task.getId()));
        historyManager.remove(taskManager.removeEpicByID(epic.getId()));
        historyManager.remove(taskManager.removeSubtaskByID(subtask.getId()));
    }

}