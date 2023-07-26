package finaltask;

import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;


import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.

        Task task1 = new Task("Задача №1", "Описание задачи №1");
        Task task2 = new Task("Задача №2", "Описание задачи №2");

        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1");
        Epic epic2 = new Epic("Эпик №2", "Описание эпика №2");

        Subtask subtask1_1 = new Subtask("Подзадача №1", "Описание подзадачи №1");
        Subtask subtask1_2 = new Subtask("Подзадача №2", "Описание подзадачи №2");
        Subtask subtask2_1 = new Subtask("Подзадача №1", "Описание подзадачи №1");



        task1 = taskManager.createTask(task1);
        task2 = taskManager.createTask(task2);

        epic1 = taskManager.createEpic(epic1);
        epic2 = taskManager.createEpic(epic2);

        subtask1_1 = (Subtask) taskManager.createSubtask(subtask1_1); // ПОЧЕМУ?
        subtask1_2 = (Subtask) taskManager.createSubtask(subtask1_2); // ПОЧЕМУ?
        subtask2_1 = (Subtask) taskManager.createSubtask(subtask2_1); // ПОЧЕМУ?


        // Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        // И, наконец, попробуйте удалить одну из задач и один из эпиков.

        ArrayList<Task> tasks = taskManager.getAllTasks();

        Task modifiedTask1 = taskManager.getTaskByID(task1.getId());
        modifiedTask1.setStatus("IN_PROGRESS");
        taskManager.updateTask(modifiedTask1);

        Task completedTask2 = taskManager.getTaskByID(task2.getId());
        completedTask2.setStatus("DONE");
        taskManager.updateTask(completedTask2);

        // Распечатайте списки эпиков, задач и подзадач

        System.out.println(taskManager.getTaskByID(task1.getId()));
        System.out.println(taskManager.getTaskByID(task2.getId()));
        System.out.println(taskManager.getAllTasks());

        System.out.println(epic1);
        System.out.println(epic2);

        System.out.println(subtask1_1);
        System.out.println(subtask1_2);
        System.out.println(subtask2_1);

    }
}
