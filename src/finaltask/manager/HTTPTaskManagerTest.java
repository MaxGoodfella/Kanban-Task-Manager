package finaltask.manager;

import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;
import finaltask.tasks.TaskStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskManagerTest {
    private static HTTPTaskManager httpTaskManager;

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    private static String serverURL = "http://localhost:8078/";

    @BeforeAll
    public static void setUp() {
        httpTaskManager = new HTTPTaskManager(serverURL);
    }

    @Test
    public void testSaveAndLoad() {

        // создали задачи и пр. и бахнули в историю

        LocalDateTime task1StartTime = LocalDateTime.of(2023, 11, 10, 11, 30);
        Duration task1Duration = Duration.ofMinutes(20);
        Task task1 = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW, task1StartTime, task1Duration);

        LocalDateTime task2StartTime = LocalDateTime.of(2023, 11, 10, 12, 0);
        Duration task2Duration = Duration.ofMinutes(30);
        Task task2 = new Task("Задача №2", "Описание задачи №2", TaskStatus.IN_PROGRESS, task2StartTime, task2Duration);

        task1 = httpTaskManager.createTask(task1);
        task2 = httpTaskManager.createTask(task2);



        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1", TaskStatus.NEW);
        Epic epic2 = new Epic("Эпик №2", "Описание эпика №2", TaskStatus.NEW);

        epic1 = httpTaskManager.createEpic(epic1);
        epic2 = httpTaskManager.createEpic(epic2);



        LocalDateTime subtask1_1StartTime = LocalDateTime.of(2023, 11, 10, 15, 0);
        Duration subtask1_1Duration = Duration.ofMinutes(15);
        Subtask subtask1_1 = new Subtask("Подзадача №1.1", "Описание подзадачи №1.1", TaskStatus.NEW, epic1.getId(), subtask1_1StartTime, subtask1_1Duration);

        LocalDateTime subtask1_2StartTime = LocalDateTime.of(2023, 11, 10, 15, 30);
        Duration subtask1_2Duration = Duration.ofMinutes(10);
        Subtask subtask1_2 = new Subtask("Подзадача №1.2", "Описание подзадачи №1.2", TaskStatus.NEW, epic1.getId(), subtask1_2StartTime, subtask1_2Duration);

        LocalDateTime subtask1_3StartTime = LocalDateTime.of(2023, 11, 10, 16, 0);
        Duration subtask1_3Duration = Duration.ofMinutes(25);
        Subtask subtask1_3 = new Subtask("Подзадача №1.3", "Описание подзадачи №1.3", TaskStatus.NEW, epic1.getId(), subtask1_3StartTime, subtask1_3Duration);

        subtask1_1 = httpTaskManager.createSubtask(subtask1_1);
        subtask1_2 = httpTaskManager.createSubtask(subtask1_2);
        subtask1_3 = httpTaskManager.createSubtask(subtask1_3);

        httpTaskManager.calculateEpicStartTime(epic1.getId());
        httpTaskManager.calculateEpicDuration(epic1.getId());
        httpTaskManager.calculateEpicEndTime(epic1.getId());


        LocalDateTime subtask2_1StartTime = LocalDateTime.of(2023, 11, 10, 18, 0);
        Duration subtask2_1Duration = Duration.ofMinutes(15);
        Subtask subtask2_1 = new Subtask("Подзадача №2.1", "Описание подзадачи №2.1", TaskStatus.NEW, epic2.getId(), subtask2_1StartTime, subtask2_1Duration);

        subtask2_1 = httpTaskManager.createSubtask(subtask2_1);

        httpTaskManager.calculateEpicStartTime(epic2.getId());
        httpTaskManager.calculateEpicDuration(epic2.getId());
        httpTaskManager.calculateEpicEndTime(epic2.getId());


        historyManager.addTask(httpTaskManager.getTaskByID(task1.getId()));
        historyManager.addTask(httpTaskManager.getTaskByID(task2.getId()));
        historyManager.addTask(httpTaskManager.getTaskByID(task2.getId()));
        historyManager.addTask(httpTaskManager.getTaskByID(task2.getId()));
        historyManager.addTask(httpTaskManager.getTaskByID(task2.getId()));
        historyManager.addTask(httpTaskManager.getSubtaskByID(subtask1_3.getId()));
        historyManager.addTask(httpTaskManager.getSubtaskByID(subtask1_3.getId()));
        historyManager.addTask(httpTaskManager.getSubtaskByID(subtask1_1.getId()));
        historyManager.addTask(httpTaskManager.getTaskByID(task1.getId()));
        historyManager.addTask(httpTaskManager.getEpicByID(epic1.getId()));
        historyManager.addTask(httpTaskManager.getEpicByID(epic2.getId()));
        historyManager.addTask(httpTaskManager.getEpicByID(epic1.getId()));
        historyManager.addTask(httpTaskManager.getEpicByID(epic2.getId()));
        historyManager.addTask(httpTaskManager.getTaskByID(task1.getId()));
        historyManager.addTask(httpTaskManager.getTaskByID(task1.getId()));
        historyManager.addTask(httpTaskManager.getTaskByID(task1.getId()));
        historyManager.addTask(httpTaskManager.getSubtaskByID(subtask1_3.getId()));
        historyManager.addTask(httpTaskManager.getSubtaskByID(subtask1_3.getId()));
        historyManager.addTask(httpTaskManager.getTaskByID(task1.getId()));
        historyManager.addTask(httpTaskManager.getTaskByID(task1.getId()));
        historyManager.addTask(httpTaskManager.getEpicByID(epic2.getId()));
        historyManager.addTask(httpTaskManager.getSubtaskByID(subtask1_2.getId()));
        historyManager.addTask(httpTaskManager.getSubtaskByID(subtask2_1.getId()));



        // сохранили на сервер
        httpTaskManager.save();

        // сохранили в листы, чтобы было потом с чем сравнивать
        List<Task> expectedTasks = new ArrayList<>(httpTaskManager.getAllTasks());
        List<Epic> expectedEpics = new ArrayList<>(httpTaskManager.getAllEpics());
        List<Subtask> expectedSubtasks = new ArrayList<>(httpTaskManager.getAllSubtasks());
        List<Task> expectedHistory = new ArrayList<>(httpTaskManager.getHistory());


        // очистили, чтобы загружать чисто с сервера
        httpTaskManager.taskStorage.clear();
        httpTaskManager.epicStorage.clear();
        httpTaskManager.subtaskStorage.clear();

        // загружаем
        httpTaskManager.loadFromServer();


        assertEquals(expectedTasks, httpTaskManager.getAllTasks(), "Список задач не совпадает.");
        assertEquals(expectedEpics, httpTaskManager.getAllEpics(), "Список эпиков не совпадает.");
        assertEquals(expectedSubtasks, httpTaskManager.getAllSubtasks(), "Список подзадач не совпадает.");
        assertEquals(expectedHistory, httpTaskManager.getHistory(), "История не совпадает.");

    }

}