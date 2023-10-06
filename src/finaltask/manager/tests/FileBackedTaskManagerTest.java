package finaltask.manager.tests;

import finaltask.manager.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {



    private FileBackedTaskManager taskManager;

    private InMemoryHistoryManager historyManager;

    private File file;



    @BeforeEach
    void setUpSecond() {
        file = new File("/Users/MaximGuseynov/dev3/sprint7/java-kanban/src/resources/sprint7/test.txt");

        historyManager = new InMemoryHistoryManager();

        taskManager = new FileBackedTaskManager(file, historyManager);


        assertTrue(file.exists(), "Файл не существует");


        Task task1 = new Task("Задача №1", "Описание задачи №1");
        Task task2 = new Task("Задача №2", "Описание задачи №2");
        task1 = taskManager.createTask(task1);
        task2 = taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1", TaskStatus.NEW);
        Epic epic2 = new Epic("Эпик №2", "Описание эпика №2", TaskStatus.NEW);
        epic1 = taskManager.createEpic(epic1);
        epic2 = taskManager.createEpic(epic2);

        Subtask subtask1_1 = new Subtask("Подзадача №1.1", "Описание подзадачи №1.1", TaskStatus.NEW, epic1.getId());
        Subtask subtask1_2 = new Subtask("Подзадача №1.2", "Описание подзадачи №1.2", TaskStatus.NEW, epic1.getId());
        Subtask subtask1_3 = new Subtask("Подзадача №1.3", "Описание подзадачи №1.3", TaskStatus.NEW, epic1.getId());
        subtask1_1 = taskManager.createSubtask(subtask1_1);
        subtask1_2 = taskManager.createSubtask(subtask1_2);
        subtask1_3 = taskManager.createSubtask(subtask1_3);


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


    }

    @Test
    public void testSave() {

        taskManager.save();

        List<String> fileLines = readLinesFromFile(file);
        List<String> expectedLines = Arrays.asList(
                "id,type,name,status,description,epic",
                "1,TASK,Задача №1,NEW,Описание задачи №1,",
                "2,TASK,Задача №2,NEW,Описание задачи №2,",
                "3,EPIC,Эпик №1,NEW,Описание эпика №1,",
                "4,EPIC,Эпик №2,NEW,Описание эпика №2,",
                "5,SUBTASK,Подзадача №1.1,NEW,Описание подзадачи №1.1,3",
                "6,SUBTASK,Подзадача №1.2,NEW,Описание подзадачи №1.2,3",
                "7,SUBTASK,Подзадача №1.3,NEW,Описание подзадачи №1.3,3",
                "",
                "2,5,3,7,1,4,6"
        );

        assertEquals(expectedLines, fileLines);
        assertEquals(expectedLines.size(), fileLines.size());

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

        assertEquals(taskManager.getHistory(), loadedManager.getHistory(), "История не совпадает.");

                /*

        assertEquals(taskManager.getAllTasks(), loadedManager.getAllTasks());
        assertEquals(taskManager.getAllEpics(), loadedManager.getAllEpics());
        assertEquals(taskManager.getAllSubtasks(), loadedManager.getAllSubtasks());


        assertEquals(taskManager.getHistory(), loadedManager.getHistory());

         */
    }

}