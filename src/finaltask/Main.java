package finaltask;

import finaltask.manager.*;
import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;
import finaltask.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getHistoryDefault();
        //


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


        epic1.addSubtaskID(subtask1_1.getId());
        epic1.addSubtaskID(subtask1_2.getId());
        epic2.addSubtaskID(subtask1_3.getId());

        taskManager.updateAllEpicsStatus();


        System.out.println(taskManager.getTaskByID(task1.getId()));
        System.out.println(taskManager.getTaskByID(task2.getId()));
        System.out.println(taskManager.getAllTasks());
        System.out.println();

        System.out.println(taskManager.getEpicByID(epic1.getId()));
        System.out.println(taskManager.getEpicByID(epic2.getId()));
        System.out.println(taskManager.getAllEpics());
        System.out.println();

        System.out.println(taskManager.getSubtaskByID(subtask1_1.getId()));
        System.out.println(taskManager.getSubtaskByID(subtask1_2.getId()));
        System.out.println(taskManager.getSubtaskByID(subtask1_3.getId()));
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();


        ArrayList<Task> tasks = taskManager.getAllTasks();

        Task modifiedTask1 = taskManager.getTaskByID(task1.getId());
        modifiedTask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(modifiedTask1);

        Task completedTask2 = taskManager.getTaskByID(task2.getId());
        completedTask2.setStatus(TaskStatus.DONE);
        taskManager.updateTask(completedTask2);


        ArrayList<Subtask> subtasks = taskManager.getAllSubtasks();

        Subtask modifiedSubtask1 = taskManager.getSubtaskByID(subtask1_1.getId());
        modifiedSubtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(modifiedSubtask1);

        Subtask modifiedSubtask2 = taskManager.getSubtaskByID(subtask1_2.getId());
        modifiedSubtask2.setStatus(TaskStatus.NEW);
        taskManager.updateSubtask(modifiedSubtask2);

        Subtask modifiedSubtask3 = taskManager.getSubtaskByID(subtask1_3.getId());
        modifiedSubtask3.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(modifiedSubtask3);


        ArrayList<Epic> epics = taskManager.getAllEpics();
        for (Epic epic : epics) {
            taskManager.updateEpicStatus(epic.getId());
        }


        System.out.println(taskManager.getTaskByID(task1.getId()));
        System.out.println(taskManager.getTaskByID(task2.getId()));
        System.out.println(taskManager.getAllTasks());
        System.out.println();

        System.out.println(taskManager.getEpicByID(epic1.getId()));
        System.out.println(taskManager.getEpicByID(epic2.getId()));
        System.out.println(taskManager.getAllEpics());
        System.out.println();

        System.out.println(taskManager.getSubtaskByID(subtask1_1.getId()));
        System.out.println(taskManager.getSubtaskByID(subtask1_2.getId()));
        System.out.println(taskManager.getSubtaskByID(subtask1_3.getId()));
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();


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

        System.out.println("История просмотров до удаления:");
        List<Task> historyBefore = historyManager.getHistory();
        for (Task task : historyBefore) {
            System.out.println(task);
        }


        taskManager.removeTaskByID(task1.getId());

        System.out.println("История просмотров после удаления (1):");
        List<Task> historyAfter1 = historyManager.getHistory();
        for (Task task : historyAfter1) {
            System.out.println(task);
        }


        taskManager.removeEpicByID(epic1.getId());

        System.out.println("История просмотров после удаления (2):");
        List<Task> historyAfter2 = historyManager.getHistory();
        for (Task task : historyAfter2) {
            System.out.println(task);
        }


        // FileBackedTaskManagerTest.main(args);



    }
}