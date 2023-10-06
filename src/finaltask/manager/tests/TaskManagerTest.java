package finaltask.manager.tests;

import finaltask.manager.Managers;
import finaltask.manager.TaskManager;
import finaltask.tasks.Epic;
import finaltask.tasks.Task;
import finaltask.tasks.Subtask;
import finaltask.tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected Task task;
    protected Epic epic;
    protected Subtask subtask;


    @BeforeEach
    void init() {
        taskManager = (T) Managers.getDefault(); // !

        task = new Task("Task 1", "Description 1");
        taskManager.createTask(task);

        epic = new Epic("Epic 1", "Epic Description 1");
        taskManager.createEpic(epic);

        subtask = new Subtask("Subtask 1", "Subtask Description 1", TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask);
    }

    @Test
    void testCreateTask() {


        Task newTask = new Task("Test Task", "Test Task Description");
        Task createdTask = taskManager.createTask(newTask);

        assertNotNull(createdTask, "Задача не найдена.");
        assertEquals(newTask, createdTask, "Задачи не совпадают.");
        assertNotNull(createdTask.getId(), "Идентификатор задачи не должен быть null");

        Task anotherCreatedTask = taskManager.getTaskByID(createdTask.getId());
        assertNotNull(anotherCreatedTask, "Полученная задача не должна быть null");
        assertEquals(createdTask, anotherCreatedTask, "Полученная задача должна совпадать с созданной задачей");

        List<Task> allTasks = taskManager.getAllTasks();
        assertEquals(2, allTasks.size(), "Количество задач после создания не совпадает"); // раюотает, но вот здесь выдает ошибку 
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "История пустая");
        assertEquals(1, tasks.size(), "История не соответствует размеру");
        assertEquals(task, tasks.get(0), "Задачи не совпадают");
    }





    @Test
    public void testCreateEpic() {

    }
}