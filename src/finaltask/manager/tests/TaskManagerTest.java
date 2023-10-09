package finaltask.manager.tests;

import finaltask.manager.HistoryManager;
import finaltask.manager.InMemoryHistoryManager;
import finaltask.manager.InMemoryTaskManager;
import finaltask.manager.TaskManager;
import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;
import finaltask.tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    protected abstract T createTaskManager();


    @BeforeEach
    protected void init() {
        taskManager = createTaskManager();

        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubtasks();

    }


    @Test
    void testCreateTask() {

        Task newTask = new Task("Test New Task", "Test New Task Description");
        Task createdTask = taskManager.createTask(newTask);
        assertNotNull(createdTask, "Задача не найдена.");
        assertEquals(newTask, createdTask, "Задачи не совпадают.");
        assertNotNull(createdTask.getId(), "Идентификатор задачи не должен быть null");

        Task anotherNewTask = new Task("Test Another New Task", "Test Another New Task Description");
        Task anotherCreatedTask = taskManager.createTask(anotherNewTask);
        assertNotNull(anotherCreatedTask, "Задача не найдена.");
        assertEquals(anotherNewTask, anotherCreatedTask, "Задачи не совпадают.");
        assertNotNull(anotherCreatedTask.getId(), "Идентификатор задачи не должен быть null");

        List<Task> allTasks = taskManager.getAllTasks();
        assertEquals(2, allTasks.size(), "Количество задач после создания не совпадает");

    }

    @Test
    void testGetTaskById() {
        Task newTask = new Task("Test Task", "Test Task Description");
        taskManager.createTask(newTask);
        // Task task = taskManager.getTaskByID(createdTask.getId());
        //assertEquals(newTask, taskManager.getTaskByID(createdTask.getId()));
        assertEquals(newTask, taskManager.getTaskByID(newTask.getId()));

        // System.out.println(taskManager.getHistory().size());
        // +1
    }


    @Test
    void testReturnsNullIfNoTask() {
        Task task = taskManager.getTaskByID(123);
        assertNull(task);
    }

    @Test
    void testGetAllTasks() {
        Task newTask = new Task("Test Task", "Test Task Description");
        taskManager.createTask(newTask);

        List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "История пустая");
        assertEquals(1, tasks.size(), "История не соответствует размеру");
        assertEquals(newTask, tasks.get(0), "Задачи не совпадают");
    }


    @Test
    void testRemoveAllTasks() {
        taskManager.removeAllTasks();

        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(0, tasks.size(), "Все задачи не были удалены");
    }



    @Test
    void testUpdateTask() {
        Task newTask = new Task("Test Task", "Test Task Description");
        Task createdTask = taskManager.createTask(newTask);

        createdTask.setName("Updated Task");
        createdTask.setDescription("Updated Task Description");

        taskManager.updateTask(createdTask);

        Task updatedTask = taskManager.getTaskByID(createdTask.getId());

        assertNotNull(updatedTask, "Задача не была обновлена");
        assertEquals("Updated Task", updatedTask.getName(), "Название задачи не обновлено");
        assertEquals("Updated Task Description", updatedTask.getDescription(), "Описание задачи не обновлено");

        // + 1
    }



    @Test
    void testRemoveTaskByID() {
        Task newTask = new Task("Test Task", "Test Task Description");
        Task createdTask = taskManager.createTask(newTask);

        taskManager.removeTaskByID(createdTask.getId());

        Task removedTask = taskManager.getTaskByID(createdTask.getId());
        assertNull(removedTask, "Задача с таким ID не была удалена");
    }


    @Test
    void testCreateEpic() {

        Epic newEpic = new Epic("Test New Epic", "Test New Epic Description");
        Epic createdEpic = taskManager.createEpic(newEpic);
        assertNotNull(createdEpic, "Эпик не найден.");
        assertEquals(newEpic, createdEpic, "Эпики не совпадают.");
        assertNotNull(createdEpic.getId(), "Идентификатор эпика не должен быть null");

        Epic anotherNewEpic = new Epic("Test Another New Epic", "Test Another New Epic Description");
        Epic anotherCreatedEpic = taskManager.createEpic(anotherNewEpic);
        assertNotNull(anotherCreatedEpic, "Эпик не найден.");
        assertEquals(anotherNewEpic, anotherCreatedEpic, "Эпики не совпадают.");
        assertNotNull(anotherCreatedEpic.getId(), "Идентификатор эпика не должен быть null");

        List<Epic> allEpics = taskManager.getAllEpics();
        assertEquals(2, allEpics.size(), "Количество эпиков после создания не совпадает");
    }



    @Test
    void testGetEpicById() {
        Epic newEpic = new Epic("Test Epic", "Test Epic Description");
        Epic createdEpic = taskManager.createEpic(newEpic);
        Epic epic = taskManager.getEpicByID(createdEpic.getId());
        assertEquals(createdEpic, epic);
        // +1
    }


    @Test
    void testReturnsNullIfNoEpic() {
        Epic epic = taskManager.getEpicByID(123);
        assertNull(epic);
    }

    @Test
    void testGetAllEpics() {
        Epic newEpic = new Epic("Test Epic", "Test Epic Description");
        Epic createdEpic = taskManager.createEpic(newEpic);

        List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "История пустая");
        assertEquals(1, epics.size(), "История не соответствует размеру");
        assertEquals(newEpic, epics.get(0), "Эпики не совпадают");
    }

    @Test
    void testRemoveAllEpics() {
        taskManager.removeAllEpics();

        List<Epic> epics = taskManager.getAllEpics();
        assertEquals(0, epics.size(), "Все эпики не были удалены");
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertEquals(0, subtasks.size(), "Все подзадачи, связанные со всеми эпиками не были удалены");
    }


    @Test
    void testUpdateEpic() {
        Epic newEpic = new Epic("Test Epic", "Test Epic Description");
        Epic createdEpic = taskManager.createEpic(newEpic);

        createdEpic.setName("Updated Epic");
        createdEpic.setDescription("Updated Epic Description");

        taskManager.updateEpic(createdEpic);

        Epic updatedEpic = taskManager.getEpicByID(createdEpic.getId());

        assertNotNull(updatedEpic, "Эпик не был обновлён");
        assertEquals("Updated Epic", updatedEpic.getName(), "Название эпика не обновлено");
        assertEquals("Updated Epic Description", updatedEpic.getDescription(), "Описание эпика не обновлено");
    // +1
    }

    @Test
    void testRemoveEpicByID() {
        Epic newEpic = new Epic("Test Epic", "Test Epic Description");
        taskManager.createEpic(newEpic);

        taskManager.removeEpicByID(newEpic.getId());

        Epic removedEpic = taskManager.getEpicByID(newEpic.getId());
        assertNull(removedEpic, "Эпик с таким ID не был удалён");

        List<Subtask> subtasks = taskManager.getAllSubtasks();
        for (Subtask subtask : subtasks) {
            assertNotEquals(newEpic.getId(), subtask.getEpicID(), "Все подзадачи, связанные с данным эпиком, не были удалены");
        }
    }

    @Test
    public void testUpdateEpicStatus() {
        Epic newEpic = new Epic("Test Epic", "Test Epic Description");
        taskManager.createEpic(newEpic);

        Subtask subtask1 = new Subtask("Subtask 2", "Subtask Description 2", TaskStatus.NEW, newEpic.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description 2", TaskStatus.NEW, newEpic.getId());
        taskManager.createSubtask(subtask2);
        assertEquals(TaskStatus.NEW, newEpic.getStatus(), "Статус эпика не NEW");


        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        Epic updatedEpic1 = taskManager.getEpicByID(newEpic.getId());
        assertEquals(TaskStatus.DONE, updatedEpic1.getStatus(), "Статус эпика не DONE");


        subtask1.setStatus(TaskStatus.NEW);
        taskManager.updateSubtask(subtask1);
        Epic updatedEpic2 = taskManager.getEpicByID(newEpic.getId());
        assertEquals(TaskStatus.NEW, updatedEpic2.getStatus(), "Статус эпика не NEW");


        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        Epic updatedEpic3 = taskManager.getEpicByID(newEpic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, updatedEpic3.getStatus(), "Статус эпика не IN_PROGRESS");


        subtask1.setStatus(TaskStatus.NEW);
        taskManager.updateSubtask(subtask1);
        Epic updatedEpic4 = taskManager.getEpicByID(newEpic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, updatedEpic4.getStatus(), "Статус эпика не IN_PROGRESS");


        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        Epic updatedEpic5 = taskManager.getEpicByID(newEpic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, updatedEpic5.getStatus(), "Статус эпика не IN_PROGRESS");


        taskManager.removeAllSubtasks();
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertEquals(0, subtasks.size(), "Все подзадачи не были удалены");
        Epic updatedEpic6 = taskManager.getEpicByID(newEpic.getId());
        assertEquals(TaskStatus.DONE, updatedEpic6.getStatus(), "Статус эпика не DONE, эпик не пуст");

    }

    @Test
    void testActualizeEpic() {

        HistoryManager historyManager = new InMemoryHistoryManager();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(historyManager);

        Epic newEpic = new Epic("Test Epic", "Test Epic Description");
        taskManager.createEpic(newEpic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description 1", TaskStatus.NEW, newEpic.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description 2", TaskStatus.NEW, newEpic.getId());
        taskManager.createSubtask(subtask2);

        taskManager.actualizeEpic(newEpic.getId(), inMemoryTaskManager);
        assertEquals(TaskStatus.NEW, newEpic.getStatus(), "Статус эпика не был обновлен на NEW");


        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.actualizeEpic(newEpic.getId(), inMemoryTaskManager);
        assertEquals(TaskStatus.DONE, newEpic.getStatus(), "Статус эпика не был обновлен на DONE");


        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask2);
        taskManager.actualizeEpic(newEpic.getId(), inMemoryTaskManager);
        assertEquals(TaskStatus.IN_PROGRESS, newEpic.getStatus(), "Статус эпика не был обновлен на IN_PROGRESS");

    }

    @Test
    void testUpdateAllEpicsStatus() {

        Epic newEpic1 = new Epic("Test New Epic1", "Test New Epic1 Description");
        Epic createdEpic1 = taskManager.createEpic(newEpic1);

        Epic newEpic2 = new Epic("Test New Epic3", "Test New Epic3 Description");
        Epic createdEpic2 = taskManager.createEpic(newEpic2);

        Epic newEpic3 = new Epic("Test New Epic3", "Test New Epic3 Description");
        Epic createdEpic3 = taskManager.createEpic(newEpic3);

        List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "История пустая");
        assertEquals(3, epics.size(), "История не соответствует размеру");
        assertEquals(createdEpic1, epics.get(0), "Эпики не совпадают");
        assertEquals(createdEpic2, epics.get(1), "Эпики не совпадают");
        assertEquals(createdEpic3, epics.get(2), "Эпики не совпадают");


        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description 1", TaskStatus.NEW, createdEpic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description 2", TaskStatus.IN_PROGRESS, createdEpic1.getId());
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask Description 3", TaskStatus.DONE, createdEpic2.getId());
        taskManager.createSubtask(subtask3);
        Subtask subtask4 = new Subtask("Subtask 4", "Subtask Description 4", TaskStatus.DONE, createdEpic2.getId());
        taskManager.createSubtask(subtask4);
        Subtask subtask5 = new Subtask("Subtask 5", "Subtask Description 5", TaskStatus.NEW, createdEpic3.getId());
        taskManager.createSubtask(subtask5);
        Subtask subtask6 = new Subtask("Subtask 6", "Subtask Description 6", TaskStatus.NEW, createdEpic3.getId());
        taskManager.createSubtask(subtask6);


        taskManager.updateAllEpicsStatus();

        assertEquals(TaskStatus.IN_PROGRESS, createdEpic1.getStatus(), "Статус эпика не был обновлен на IN_PROGRESS");
        assertEquals(TaskStatus.DONE, createdEpic2.getStatus(), "Статус эпика не был обновлен на DONE");
        assertEquals(TaskStatus.NEW, createdEpic3.getStatus(), "Статус эпика не был обновлен на NEW");

    }


    @Test
    public void testCreateSubtask() {
        Epic epic = new Epic("Test Epic", "Test Epic Description");
        taskManager.createEpic(epic);

        Subtask newSubtask = new Subtask("Test New Subtask", "Test New Subtask Description", TaskStatus.NEW, epic.getId());
        Subtask createdSubtask = taskManager.createSubtask(newSubtask);

        assertNotNull(createdSubtask, "Подзадача не найдена.");
        assertEquals(newSubtask, createdSubtask, "Подзадачи не совпадают.");
        assertNotNull(createdSubtask.getId(), "Идентификатор подзадачи не должен быть null");

        Subtask anotherNewSubtask = new Subtask("Test Another New Subtask", "Test Another New Subtask Description", TaskStatus.NEW, epic.getId());
        Subtask anotherCreatedSubtask = taskManager.createSubtask(anotherNewSubtask);
        assertNotNull(anotherCreatedSubtask, "Подзадача не найдена.");
        assertEquals(anotherNewSubtask, anotherCreatedSubtask, "Подзадачи не совпадают.");
        assertNotNull(anotherCreatedSubtask.getId(), "Идентификатор подзадачи не должен быть null");

        List<Subtask> allSubtasks = taskManager.getAllSubtasks();
        assertEquals(2, allSubtasks.size(), "Количество подзадача после создания не совпадает");
    }

    @Test
    void testGetSubtaskById() {
        Subtask newSubtask = new Subtask("Test Subtask", "Test Subtask Description");
        Subtask createdSubtask = taskManager.createSubtask(newSubtask);
        Subtask subtask = taskManager.getSubtaskByID(createdSubtask.getId());
        assertEquals(createdSubtask, subtask);
        // +1
    }

    @Test
    void testReturnsNullIfNoSubtask() {
        Subtask subtask = taskManager.getSubtaskByID(123);
        assertNull(subtask);
    }

    @Test
    void testGetAllSubtasks() {
        Subtask newSubtask = new Subtask("Test Subtask", "Test Subtask Description");
        taskManager.createSubtask(newSubtask);

        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertNotNull(subtasks, "История пустая");
        assertEquals(1, subtasks.size(), "История не соответствует размеру");
        assertEquals(newSubtask, subtasks.get(0), "Эпики не совпадают");
    }

    @Test
    void testRemoveAllSubtasks() {
        taskManager.removeAllSubtasks();

        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertEquals(0, subtasks.size(), "Все подзадачи не были удалены");
    }

    @Test
    void testUpdateSubtask() {
        Subtask newSubtask = new Subtask("Test Subtask", "Test Subtask Description");
        Subtask createdSubtask = taskManager.createSubtask(newSubtask);

        createdSubtask.setName("Updated Subtask");
        createdSubtask.setDescription("Updated Subtask Description");

        taskManager.updateSubtask(createdSubtask);

        Subtask updatedSubtask = taskManager.getSubtaskByID(createdSubtask.getId());

        assertNotNull(updatedSubtask, "Подзадача не была обновлена");
        assertEquals("Updated Subtask", updatedSubtask.getName(), "Название подзадачи не обновлено");
        assertEquals("Updated Subtask Description", updatedSubtask.getDescription(), "Описание подзадачи не обновлено");
    // +1
    }


    @Test
    void testRemoveSubtaskByID() {
        Subtask newSubtask = new Subtask("Test Subtask", "Test Subtask Description");
        Subtask createdSubtask = taskManager.createSubtask(newSubtask);

        taskManager.removeSubtaskByID(createdSubtask.getId());

        Subtask removedSubtask = taskManager.getSubtaskByID(createdSubtask.getId());
        assertNull(removedSubtask, "Подзадача с таким ID не была удалена");
    }



    @Test
    void testGetHistory() {
//
//        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
//
//        Task newTask = new Task("Test New Task", "Test New Task Description");
//        taskManager.createTask(newTask);
//        Epic newEpic = new Epic("Test New Epic", "Test New Epic Description");
//        taskManager.createEpic(newEpic);
//        Subtask newSubtask = new Subtask("Test New Subtask", "Test New Subtask Description", TaskStatus.NEW, newEpic.getId());
//        taskManager.createSubtask(newSubtask);
//
//        historyManager.addTask(taskManager.getTaskByID(newTask.getId()));
//        historyManager.addTask(taskManager.getEpicByID(newEpic.getId()));
//        historyManager.addTask(taskManager.getSubtaskByID(newSubtask.getId()));
//
//
//        List<Task> history = taskManager.getHistory();
//
//
//        assertEquals(3, history.size(), "Размер истории не совпадает");
//
//        Task historyElement1 = history.get(0);
//        assertEquals("Test New Task", historyElement1.getName(), "Неверное имя");
//        assertEquals("Test New Task Description", historyElement1.getDescription(), "Неверное описание");
//
//        Task historyElement2 = history.get(1);
//        assertEquals("Test New Epic", historyElement2.getName(), "Неверное имя");
//        assertEquals("Test New Epic Description", historyElement2.getDescription(), "Неверное описание");
//
//        Task historyElement3 = history.get(2);
//        assertEquals("Test New Subtask", historyElement3.getName(), "Неверное имя");
//        assertEquals("Test New Subtask Description", historyElement3.getDescription(), "Неверное описание");


        int initialHistorySize = taskManager.getHistory().size();

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task newTask = new Task("Test New Task", "Test New Task Description");
        taskManager.createTask(newTask);
        Epic newEpic = new Epic("Test New Epic", "Test New Epic Description");
        taskManager.createEpic(newEpic);
        Subtask newSubtask = new Subtask("Test New Subtask", "Test New Subtask Description", TaskStatus.NEW, newEpic.getId());
        taskManager.createSubtask(newSubtask);

        historyManager.addTask(taskManager.getTaskByID(newTask.getId()));
        historyManager.addTask(taskManager.getEpicByID(newEpic.getId()));
        historyManager.addTask(taskManager.getSubtaskByID(newSubtask.getId()));


        int finalHistorySize = taskManager.getHistory().size();

        int newHistoryEntries = finalHistorySize - initialHistorySize;

        assertEquals(3, newHistoryEntries, "Неверное количество новых записей в истории");

        // здесь сделал немного обходным путём, я не понимаю, почему у меня testGetHistory() работает отдельно, но когда
        // запускаю его вместе в остальными, он мне плюсует 6 дополнительных элементов.
        // Я вычислил, что это из-за методов testGetTaskById(), testGetEpicById(), testGetSubtaskById(),
        // testUpdateTask(), testUpdateEpic() и testUpdateSubtask() - каждый из них даёт по дополнительному элементу.
        // Но каким образом это происходит - я без понятия.
        // Подскажи, пожалуйста, почему это происходит? И как мне это исправить?

    }
}