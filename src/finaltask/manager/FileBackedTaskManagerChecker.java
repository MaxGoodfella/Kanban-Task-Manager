package finaltask.manager;

import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;
import finaltask.tasks.TaskStatus;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class FileBackedTaskManagerChecker {



    public static void main(String[] args) {

        File file = new File("/Users/MaximGuseynov/dev3/sprint8/java-kanban/src/resources/sprint7/test.txt");


        HistoryManager historyManager = new InMemoryHistoryManager();


        FileBackedTaskManager taskManager1 = FileBackedTaskManager.loadFromFile(file, historyManager);


        LocalDateTime task1StartTime = LocalDateTime.of(2023, 11, 10, 11, 30);
        Duration task1Duration = Duration.ofMinutes(20);
        Task task1 = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW, task1StartTime, task1Duration);

        LocalDateTime task2StartTime = LocalDateTime.of(2023, 11, 10, 12, 0);
        Duration task2Duration = Duration.ofMinutes(30);
        Task task2 = new Task("Задача №2", "Описание задачи №2", TaskStatus.IN_PROGRESS, task2StartTime, task2Duration);

        task1 = taskManager1.createTask(task1);
        task2 = taskManager1.createTask(task2);



        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1", TaskStatus.NEW);
        Epic epic2 = new Epic("Эпик №2", "Описание эпика №2", TaskStatus.NEW);

        epic1 = taskManager1.createEpic(epic1);
        epic2 = taskManager1.createEpic(epic2);



        LocalDateTime subtask1_1StartTime = LocalDateTime.of(2023, 11, 10, 15, 0);
        Duration subtask1_1Duration = Duration.ofMinutes(15);
        Subtask subtask1_1 = new Subtask("Подзадача №1.1", "Описание подзадачи №1.1", TaskStatus.NEW, epic1.getId(), subtask1_1StartTime, subtask1_1Duration);

        LocalDateTime subtask1_2StartTime = LocalDateTime.of(2023, 11, 10, 15, 30);
        Duration subtask1_2Duration = Duration.ofMinutes(10);
        Subtask subtask1_2 = new Subtask("Подзадача №1.2", "Описание подзадачи №1.2", TaskStatus.NEW, epic1.getId(), subtask1_2StartTime, subtask1_2Duration);

        LocalDateTime subtask1_3StartTime = LocalDateTime.of(2023, 11, 10, 16, 0);
        Duration subtask1_3Duration = Duration.ofMinutes(25);
        Subtask subtask1_3 = new Subtask("Подзадача №1.3", "Описание подзадачи №1.3", TaskStatus.NEW, epic1.getId(), subtask1_3StartTime, subtask1_3Duration);

        subtask1_1 = taskManager1.createSubtask(subtask1_1);
        subtask1_2 = taskManager1.createSubtask(subtask1_2);
        subtask1_3 = taskManager1.createSubtask(subtask1_3);

        taskManager1.calculateEpicStartTime(epic1.getId());
        taskManager1.calculateEpicDuration(epic1.getId());
        taskManager1.calculateEpicEndTime(epic1.getId());


        LocalDateTime subtask2_1StartTime = LocalDateTime.of(2023, 11, 10, 18, 0);
        Duration subtask2_1Duration = Duration.ofMinutes(15);
        Subtask subtask2_1 = new Subtask("Подзадача №2.1", "Описание подзадачи №2.1", TaskStatus.NEW, epic2.getId(), subtask2_1StartTime, subtask2_1Duration);

        subtask2_1 = taskManager1.createSubtask(subtask2_1);

        taskManager1.calculateEpicStartTime(epic2.getId());
        taskManager1.calculateEpicDuration(epic2.getId());
        taskManager1.calculateEpicEndTime(epic2.getId());



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
        historyManager.addTask(taskManager1.getSubtaskByID(subtask2_1.getId()));



        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(file, historyManager);


        if (taskManager1.taskStorage.size() != taskManager2.taskStorage.size()) {
            System.out.println("Ошибка сравнения количества задач");
            return;
        }
        if (taskManager1.epicStorage.size() != taskManager2.epicStorage.size()) {
            System.out.println("Ошибка сравнения количества эпиков");
            return;
        }
        if (taskManager1.subtaskStorage.size() != taskManager2.subtaskStorage.size()) {
            System.out.println("Ошибка сравнения количества подзадач");
            return;
        }


        for (Map.Entry<Integer, Task> taskEntry : taskManager1.taskStorage.entrySet()) {
            Integer taskId = taskEntry.getKey();
            Task taskFromSecondStorage = taskManager2.taskStorage.get(taskId);
            if (!taskEntry.getValue().equals(taskFromSecondStorage)) {
                System.out.println("Ошибка сравнения задач");
                return;
            }
        }

        for (Map.Entry<Integer, Epic> epicEntry : taskManager1.epicStorage.entrySet()) {
            Integer epicId = epicEntry.getKey();
            Epic epicFromSecondStorage = taskManager2.epicStorage.get(epicId);
            if (!epicEntry.getValue().equals(epicFromSecondStorage)) {
                System.out.println("Ошибка сравнения эпиков");
                return;
            }
        }

        for (Map.Entry<Integer, Subtask> subtaskEntry : taskManager1.subtaskStorage.entrySet()) {
            Integer subtaskId = subtaskEntry.getKey();
            Subtask subtaskFromSecondStorage = taskManager2.subtaskStorage.get(subtaskId);
            if (!subtaskEntry.getValue().equals(subtaskFromSecondStorage)) {
                System.out.println("Ошибка сравнения подзадач");
                return;
            }
        }


        System.out.println("Запись и чтение прошли успешно: результаты совпадают");


    }
}