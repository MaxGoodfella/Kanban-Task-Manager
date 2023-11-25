package finaltask.tests;

import finaltask.manager.HistoryManager;
import finaltask.manager.InMemoryTaskManager;
import finaltask.manager.Managers;
import finaltask.manager.TaskManager;
import finaltask.server.HTTPTaskServer;
import finaltask.server.KVServer;
import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;
import finaltask.tasks.TaskStatus;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private HistoryManager historyManager;
    private TaskManager taskManager;

    private static KVServer kvServer;
    private static HTTPTaskServer server;

    private Task task;
    private Epic epic;
    private Subtask subtask;


    @BeforeAll
    static void init() throws IOException {
        kvServer = new KVServer();
        kvServer.start();

        server = new HTTPTaskServer();
        server.start();
    }

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

    @AfterAll
    public static void tearDown() throws InterruptedException {
        server.stop(0);
        Thread.sleep(2000);
        kvServer.stop();
    }

    @AfterEach
    void clean() {
        historyManager.remove(taskManager.removeTaskByID(task.getId()));
        historyManager.remove(taskManager.removeEpicByID(epic.getId()));
        historyManager.remove(taskManager.removeSubtaskByID(subtask.getId()));
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
        historyManager.remove(taskManager.removeSubtaskByID(subtask.getId()));


        List<Task> history = historyManager.getHistory();
        System.out.println(history);
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

}