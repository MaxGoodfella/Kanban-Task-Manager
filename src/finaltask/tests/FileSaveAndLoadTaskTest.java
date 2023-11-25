package finaltask.tests;

import finaltask.manager.FileBackedTaskManager;
import finaltask.manager.InMemoryHistoryManager;
import finaltask.manager.exceptions.ManagerSaveException;
import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;
import finaltask.tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileSaveAndLoadTaskTest {

    File file = new File("java-kanban/src/resources/sprint7/test.txt");

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    FileBackedTaskManager taskManager = new FileBackedTaskManager(file, historyManager);

    @BeforeEach
    void setUp() {

        assertTrue(file.exists(), "Файл не существует");

        LocalDateTime task1StartTime = LocalDateTime.of(2023, 11, 10, 11, 30);
        Duration task1Duration = Duration.ofMinutes(20);
        Task task1 = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW, task1StartTime, task1Duration);

        LocalDateTime task2StartTime = LocalDateTime.of(2023, 11, 10, 12, 0);
        Duration task2Duration = Duration.ofMinutes(30);
        Task task2 = new Task("Задача №2", "Описание задачи №2", TaskStatus.IN_PROGRESS, task2StartTime, task2Duration);

        task1 = taskManager.createTask(task1);
        task2 = taskManager.createTask(task2);



        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1", TaskStatus.NEW);
        Epic epic2 = new Epic("Эпик №2", "Описание эпика №2", TaskStatus.NEW);

        epic1 = taskManager.createEpic(epic1);
        epic2 = taskManager.createEpic(epic2);



        LocalDateTime subtask1_1StartTime = LocalDateTime.of(2023, 11, 10, 15, 0);
        Duration subtask1_1Duration = Duration.ofMinutes(15);
        Subtask subtask1_1 = new Subtask("Подзадача №1.1", "Описание подзадачи №1.1", TaskStatus.NEW, epic1.getId(), subtask1_1StartTime, subtask1_1Duration);

        LocalDateTime subtask1_2StartTime = LocalDateTime.of(2023, 11, 10, 15, 30);
        Duration subtask1_2Duration = Duration.ofMinutes(10);
        Subtask subtask1_2 = new Subtask("Подзадача №1.2", "Описание подзадачи №1.2", TaskStatus.NEW, epic1.getId(), subtask1_2StartTime, subtask1_2Duration);

        LocalDateTime subtask1_3StartTime = LocalDateTime.of(2023, 11, 10, 16, 0);
        Duration subtask1_3Duration = Duration.ofMinutes(25);
        Subtask subtask1_3 = new Subtask("Подзадача №1.3", "Описание подзадачи №1.3", TaskStatus.NEW, epic1.getId(), subtask1_3StartTime, subtask1_3Duration);

        subtask1_1 = taskManager.createSubtask(subtask1_1);
        subtask1_2 = taskManager.createSubtask(subtask1_2);
        subtask1_3 = taskManager.createSubtask(subtask1_3);

        taskManager.calculateEpicStartTime(epic1.getId());
        taskManager.calculateEpicDuration(epic1.getId());
        taskManager.calculateEpicEndTime(epic1.getId());


        LocalDateTime subtask2_1StartTime = LocalDateTime.of(2023, 11, 10, 18, 0);
        Duration subtask2_1Duration = Duration.ofMinutes(15);
        Subtask subtask2_1 = new Subtask("Подзадача №2.1", "Описание подзадачи №2.1", TaskStatus.NEW, epic2.getId(), subtask2_1StartTime, subtask2_1Duration);

        subtask2_1 = taskManager.createSubtask(subtask2_1);

        taskManager.calculateEpicStartTime(epic2.getId());
        taskManager.calculateEpicDuration(epic2.getId());
        taskManager.calculateEpicEndTime(epic2.getId());


        historyManager.addTask(taskManager.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager.getTaskByID(task2.getId()));
        historyManager.addTask(taskManager.getTaskByID(task2.getId()));
        historyManager.addTask(taskManager.getTaskByID(task2.getId()));
        historyManager.addTask(taskManager.getTaskByID(task2.getId()));
        historyManager.addTask(taskManager.getSubtaskByID(subtask1_3.getId()));
        historyManager.addTask(taskManager.getSubtaskByID(subtask1_3.getId()));
        historyManager.addTask(taskManager.getSubtaskByID(subtask1_1.getId()));
        historyManager.addTask(taskManager.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager.getEpicByID(epic1.getId()));
        historyManager.addTask(taskManager.getEpicByID(epic2.getId()));
        historyManager.addTask(taskManager.getEpicByID(epic1.getId()));
        historyManager.addTask(taskManager.getEpicByID(epic2.getId()));
        historyManager.addTask(taskManager.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager.getSubtaskByID(subtask1_3.getId()));
        historyManager.addTask(taskManager.getSubtaskByID(subtask1_3.getId()));
        historyManager.addTask(taskManager.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager.getEpicByID(epic2.getId()));
        historyManager.addTask(taskManager.getSubtaskByID(subtask1_2.getId()));
        historyManager.addTask(taskManager.getSubtaskByID(subtask2_1.getId()));

    }

    @Test
    public void testSave() {
        taskManager.save();

        List<String> fileLines = readLinesFromFile(file);
        List<String> expectedLines = Arrays.asList(
                "id,type,name,status,description,epic,start_time,duration,end_time",
                "1,TASK,Задача №1,NEW,Описание задачи №1,,2023-11-10T11:30,PT20M,2023-11-10T11:50",
                "2,TASK,Задача №2,IN_PROGRESS,Описание задачи №2,,2023-11-10T12:00,PT30M,2023-11-10T12:30",
                "3,EPIC,Эпик №1,NEW,Описание эпика №1,,2023-11-10T15:00,PT50M,2023-11-10T16:25",
                "4,EPIC,Эпик №2,NEW,Описание эпика №2,,2023-11-10T18:00,PT15M,2023-11-10T18:15",
                "5,SUBTASK,Подзадача №1.1,NEW,Описание подзадачи №1.1,3,2023-11-10T15:00,PT15M,2023-11-10T15:15",
                "6,SUBTASK,Подзадача №1.2,NEW,Описание подзадачи №1.2,3,2023-11-10T15:30,PT10M,2023-11-10T15:40",
                "7,SUBTASK,Подзадача №1.3,NEW,Описание подзадачи №1.3,3,2023-11-10T16:00,PT25M,2023-11-10T16:25",
                "8,SUBTASK,Подзадача №2.1,NEW,Описание подзадачи №2.1,4,2023-11-10T18:00,PT15M,2023-11-10T18:15",
                "",
                "2,5,3,7,1,4,6,8"
        );

        assertEquals(expectedLines, fileLines);


        assertEquals(expectedLines.size(), fileLines.size());
    }

    @Test
    public void testSaveThrowsException() {

        File file = new File("/invalid/file/path");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file, historyManager);
        try {
            taskManager.save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }

    }


    private List<String> readLinesFromFile(File file) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл", e);
        }
        return lines;
    }


    @Test
    public void testLoadFromFile() {

        taskManager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file, historyManager);

        List<Task> expectedTasks = taskManager.getAllTasks();
        List<Task> loadedTasks = loadedManager.getAllTasks();

        assertEquals(expectedTasks, loadedTasks, "Список задач не совпадает.");

        List<Epic> expectedEpics = taskManager.getAllEpics();
        List<Epic> loadedEpics = loadedManager.getAllEpics();

        assertEquals(expectedEpics, loadedEpics, "Список эпиков не совпадает.");

        List<Subtask> expectedSubtasks = taskManager.getAllSubtasks();
        List<Subtask> loadedSubtasks = loadedManager.getAllSubtasks();

        assertEquals(expectedSubtasks, loadedSubtasks, "Список подзадач не совпадает.");

        List<Task> expectedHistory = taskManager.getHistory();
        List<Task> loadedHistory = loadedManager.getHistory();
        assertEquals(expectedHistory, loadedHistory, "История не совпадает.");

    }

}