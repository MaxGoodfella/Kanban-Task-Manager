package finaltask.manager;

import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;
import finaltask.tasks.TaskStatus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileBackedTaskManagerTest {

    public static void main(String[] args) {

        File file = new File("./src/resources/sprint6/test.txt");
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


        historyManager.addTask(taskManager1.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager1.getTaskByID(task2.getId()));
        historyManager.addTask(taskManager1.getTaskByID(task2.getId()));
        historyManager.addTask(taskManager1.getTaskByID(task2.getId()));
        historyManager.addTask(taskManager1.getTaskByID(task2.getId()));
        historyManager.addTask(taskManager1.getSubtaskByID(subtask1_3.getId()));
        historyManager.addTask(taskManager1.getSubtaskByID(subtask1_3.getId()));
        historyManager.addTask(taskManager1.getSubtaskByID(subtask1_1.getId()));
        historyManager.addTask(taskManager1.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager1.getEpicByID(epic1.getId()));
        historyManager.addTask(taskManager1.getEpicByID(epic2.getId()));
        historyManager.addTask(taskManager1.getEpicByID(epic1.getId()));
        historyManager.addTask(taskManager1.getEpicByID(epic2.getId()));
        historyManager.addTask(taskManager1.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager1.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager1.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager1.getSubtaskByID(subtask1_3.getId()));
        historyManager.addTask(taskManager1.getSubtaskByID(subtask1_3.getId()));
        historyManager.addTask(taskManager1.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager1.getTaskByID(task1.getId()));
        historyManager.addTask(taskManager1.getEpicByID(epic2.getId()));
        historyManager.addTask(taskManager1.getSubtaskByID(subtask1_2.getId()));




        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(file);

//        if (taskManager1.equals(taskManager2)) {
//            System.out.println("Всё ок");
//        } else {
//            System.out.println("Не ок");
//        }

        if (taskManager1.taskStorage.size() != taskManager2.taskStorage.size()) {
            System.out.println("Не ок");
            return;
        }
        if (taskManager1.epicStorage.size() != taskManager2.epicStorage.size()) {
            System.out.println("Не ок");
            return;
        }
        if (taskManager1.subtaskStorage.size() != taskManager2.subtaskStorage.size()) {
            System.out.println("Не ок");
            return;
        }

        // оригинальная версия

/*
        for (Map.Entry<Integer, Task> taskEntry : taskManager1.taskStorage.entrySet()) {
            // Task taskFromSecondStorage = taskManager2.taskStorage.getTaskByID(taskEntry.getKey());
            Task taskFromSecondStorage = taskManager2.taskStorage.get(taskEntry.getKey());
            if (!taskEntry.equals(taskFromSecondStorage)) {
                System.out.println("Не ок");
                return;
            }
        }// он начинает выдавать "не ок", начиная с этого цикла

        for (Map.Entry<Integer, Epic> epicEntry : taskManager1.epicStorage.entrySet()) {
            // Epic epicFromSecondStorage = taskManager2.epicStorage.getEpicByID(epicEntry.getKey());
            Epic epicFromSecondStorage = taskManager2.epicStorage.get(epicEntry.getKey());
            if (!epicEntry.equals(epicFromSecondStorage)) {
                System.out.println("Не ок");
                return;
            }
        }


        for (Map.Entry<Integer, Subtask> subtaskEntry : taskManager1.subtaskStorage.entrySet()) {
            // Subtask subtaskFromSecondStorage = taskManager2.subtaskStorage.getSubtaskByID(subtaskEntry.getKey());
            Subtask subtaskFromSecondStorage = taskManager2.subtaskStorage.get(subtaskEntry.getKey());
            if (!subtaskEntry.equals(subtaskFromSecondStorage)) {
                System.out.println("Не ок");
                return;
            }
        }

 */
        /*

        for (Map.Entry<Integer, Task> taskEntry : taskManager1.taskStorage.entrySet()) {
            Integer taskId = taskEntry.getKey();
            Task taskFromSecondStorage = taskManager2.taskStorage.get(taskId);
            if (!taskEntry.getValue().equals(taskFromSecondStorage)) {
                System.out.println("Не ок");
                return;
            }
        }

        for (Map.Entry<Integer, Epic> epicEntry : taskManager1.epicStorage.entrySet()) {
            Integer epicId = epicEntry.getKey();
            Epic epicFromSecondStorage = taskManager2.epicStorage.get(epicId);
            if (!epicEntry.getValue().equals(epicFromSecondStorage)) {
                System.out.println("Не ок");
                return;
            }
        }

        for (Map.Entry<Integer, Subtask> subtaskEntry : taskManager1.subtaskStorage.entrySet()) {
            Integer subtaskId = subtaskEntry.getKey();
            Subtask subtaskFromSecondStorage = taskManager2.subtaskStorage.get(subtaskId);
            if (!subtaskEntry.getValue().equals(subtaskFromSecondStorage)) {
                System.out.println("Не ок");
                return;
            }
        }

         */


        // 



        for (Map.Entry<Integer, Task> taskEntry : taskManager1.taskStorage.entrySet()) {
            Integer taskId = taskEntry.getKey();
            Task taskFromSecondStorage = taskManager2.taskStorage.get(taskId);
            if (!taskEntry.getValue().equals(taskFromSecondStorage)) {
                System.out.println("Не ок");
                return;
            }
        }

        for (Map.Entry<Integer, Epic> epicEntry : taskManager1.epicStorage.entrySet()) {
            Integer epicId = epicEntry.getKey();
            Epic epicFromSecondStorage = taskManager2.epicStorage.get(epicId);
            if (!epicEntry.getValue().equals(epicFromSecondStorage)) {
                System.out.println("Не ок");
                return;
            }
        }

        for (Map.Entry<Integer, Subtask> subtaskEntry : taskManager1.subtaskStorage.entrySet()) {
            Integer subtaskId = subtaskEntry.getKey();
            Subtask subtaskFromSecondStorage = taskManager2.subtaskStorage.get(subtaskId);
            if (!subtaskEntry.getValue().equals(subtaskFromSecondStorage)) {
                System.out.println("Не ок");
                return;
            }
        }


    }
}