package finaltask;

import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;


import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();


        Task task1 = new Task("Задача №1", "Описание задачи №1");
        Task task2 = new Task("Задача №2", "Описание задачи №2");
        task1 = taskManager.createTask(task1);
        task2 = taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1", "NEW");
        Epic epic2 = new Epic("Эпик №2", "Описание эпика №2", "NEW");
        epic1 = taskManager.createEpic(epic1);
        epic2 = taskManager.createEpic(epic2);

        Subtask subtask1_1 = new Subtask("Подзадача №1", "Описание подзадачи №1", "NEW", epic1.getId());
        Subtask subtask1_2 = new Subtask("Подзадача №2", "Описание подзадачи №2", "NEW", epic1.getId());
        Subtask subtask2_1 = new Subtask("Подзадача №1", "Описание подзадачи №1", "NEW", epic2.getId());
        subtask1_1 = (Subtask) taskManager.createSubtask(subtask1_1); // ПОЧЕМУ?
        subtask1_2 = (Subtask) taskManager.createSubtask(subtask1_2); // ПОЧЕМУ?
        subtask2_1 = (Subtask) taskManager.createSubtask(subtask2_1); // ПОЧЕМУ?


        epic1.addSubtaskID(subtask1_1.getId());
        epic1.addSubtaskID(subtask1_2.getId());
        epic2.addSubtaskID(subtask2_1.getId());

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
        System.out.println(taskManager.getSubtaskByID(subtask2_1.getId()));
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();


        ArrayList<Task> tasks = taskManager.getAllTasks();

        Task modifiedTask1 = taskManager.getTaskByID(task1.getId());
        modifiedTask1.setStatus("IN_PROGRESS");
        taskManager.updateTask(modifiedTask1);

        Task completedTask2 = taskManager.getTaskByID(task2.getId());
        completedTask2.setStatus("DONE");
        taskManager.updateTask(completedTask2);



        ArrayList<Subtask> subtasks = taskManager.getAllSubtasks();

        Subtask modifiedSubtask1 = taskManager.getSubtaskByID(subtask1_1.getId());
        modifiedSubtask1.setStatus("IN_PROGRESS");
        taskManager.updateSubtask(modifiedSubtask1);

        Subtask modifiedSubtask2 = taskManager.getSubtaskByID(subtask1_2.getId());
        modifiedSubtask2.setStatus("NEW");
        taskManager.updateSubtask(modifiedSubtask2);

        Subtask modifiedSubtask3 = taskManager.getSubtaskByID(subtask2_1.getId());
        modifiedSubtask3.setStatus("DONE");
        taskManager.updateSubtask(modifiedSubtask3);


        ArrayList<Epic> epics = taskManager.getAllEpics();
        for (Epic epic : epics) {
            epic.updateStatus(taskManager);
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
        System.out.println(taskManager.getSubtaskByID(subtask2_1.getId()));
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();


        taskManager.removeTaskByID(task1.getId());
        System.out.println("Список задач после удаления:");
        System.out.println(taskManager.getAllTasks());

        taskManager.removeEpicByID(epic1.getId());
        System.out.println("Список эпиков после удаления:");
        System.out.println(taskManager.getAllEpics());

        System.out.println("Список подзадач после удаления:");
        System.out.println(taskManager.getAllSubtasks());
    }
}