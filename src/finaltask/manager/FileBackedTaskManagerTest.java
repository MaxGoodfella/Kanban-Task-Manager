package finaltask.manager;

import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;
import finaltask.tasks.TaskStatus;

import java.io.File;

public class FileBackedTaskManagerTest {

    public static void main(String[] args) {

        File file = new File("test.txt");
        // File file = new File("/Users/MaximGuseynov/dev3/sprint6/java-kanban/test.txt");


        HistoryManager historyManager = new InMemoryHistoryManager();

        FileBackedTaskManager taskManager1 = new FileBackedTaskManager(file, historyManager);


        Task task1 = new Task("Задача №1", "Описание задачи №1");
        Task task2 = new Task("Задача №2", "Описание задачи №2");
        task1 = taskManager1.createTask(task1);
        task2 = taskManager1.createTask(task2);

        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1", TaskStatus.NEW);
        Epic epic2 = new Epic("Эпик №2", "Описание эпика №2", TaskStatus.NEW);
        epic1 = taskManager1.createEpic(epic1);
        epic2 = taskManager1.createEpic(epic2);

        Subtask subtask1_1 = new Subtask("Подзадача №1.1", "Описание подзадачи №1.1", TaskStatus.NEW, epic1.getId());
        Subtask subtask1_2 = new Subtask("Подзадача №1.2", "Описание подзадачи №1.2", TaskStatus.NEW, epic1.getId());
        Subtask subtask1_3 = new Subtask("Подзадача №1.3", "Описание подзадачи №1.3", TaskStatus.NEW, epic1.getId());
        subtask1_1 = taskManager1.createSubtask(subtask1_1);
        subtask1_2 = taskManager1.createSubtask(subtask1_2);
        subtask1_3 = taskManager1.createSubtask(subtask1_3);



//        taskManager1.createEpic(epic1);
//        taskManager1.createTask(task1);
//        taskManager1.createSubtask(subtask1);

        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(file);

        if (taskManager1.equals(taskManager2)) {
            System.out.println("Всё ок");
        } else {
            System.out.println("Не ок");
        }

    }
}
