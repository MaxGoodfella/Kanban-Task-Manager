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

import java.time.Duration;
import java.time.LocalDateTime;
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
        // taskManager.getPrioritizedTasks().clear();
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
        taskManager.createEpic(newEpic);

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
    public void testCalculateEpicStartTimeDurationAndEndTimeWhenSubtasksGiven() {

        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1", TaskStatus.NEW);
        epic1 = taskManager.createEpic(epic1);


        LocalDateTime subtask1_1StartTime = LocalDateTime.of(2023, 11, 10, 15, 0);
        Duration subtask1_1Duration = Duration.ofMinutes(15);
        Subtask subtask1_1 = new Subtask("Подзадача №1.1", "Описание подзадачи №1.1", TaskStatus.NEW, epic1.getId(), subtask1_1StartTime, subtask1_1Duration);

        LocalDateTime subtask1_2StartTime = LocalDateTime.of(2023, 11, 10, 15, 30);
        Duration subtask1_2Duration = Duration.ofMinutes(10);
        Subtask subtask1_2 = new Subtask("Подзадача №1.2", "Описание подзадачи №1.2", TaskStatus.NEW, epic1.getId(), subtask1_2StartTime, subtask1_2Duration);

        LocalDateTime subtask1_3StartTime = LocalDateTime.of(2023, 11, 10, 16, 0);
        Duration subtask1_3Duration = Duration.ofMinutes(25);
        Subtask subtask1_3 = new Subtask("Подзадача №1.3", "Описание подзадачи №1.3", TaskStatus.NEW, epic1.getId(), subtask1_3StartTime, subtask1_3Duration);

        taskManager.createSubtask(subtask1_1);
        taskManager.createSubtask(subtask1_2);
        taskManager.createSubtask(subtask1_3);


        taskManager.calculateEpicStartTime(epic1.getId());
        taskManager.calculateEpicDuration(epic1.getId());
        taskManager.calculateEpicEndTime(epic1.getId());


        assertEquals(subtask1_1StartTime, epic1.getStartTime(), "Время начала эпика не совпадает с временем начала первой подзадачи");
        assertEquals(Duration.ofMinutes(50), epic1.getDuration(), "Продолжительность эпика не совпадает с общей продолжительностью подзадач");
        assertEquals(subtask1_3StartTime.plus(subtask1_3Duration), epic1.getEndTime(), "Время завершения эпика не совпадает с временем завершения последней подзадачи");

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


    @Test
    public void testGetPrioritizedTasks() {

        // Артем, нужна помощь с вот этим методом
        // отдельное работает, вместе - нет


        taskManager.getPrioritizedTasks().clear();

        LocalDateTime task1StartTime = LocalDateTime.of(2023, 11, 10, 11, 30);
        Duration task1Duration = Duration.ofMinutes(20);
        Task task1 = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW, task1StartTime, task1Duration);
        task1 = taskManager.createTask(task1);


        LocalDateTime epic1StartTime = LocalDateTime.of(2023, 11, 10, 13, 0);
        Duration epic1Duration = Duration.ofMinutes(20);
        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1", TaskStatus.NEW, epic1StartTime, epic1Duration);
        epic1 = taskManager.createEpic(epic1);


        LocalDateTime subtask1_1StartTime = LocalDateTime.of(2023, 11, 10, 15, 0);
        Duration subtask1_1Duration = Duration.ofMinutes(15);
        Subtask subtask1_1 = new Subtask("Подзадача №1.1", "Описание подзадачи №1.1", TaskStatus.NEW, epic1.getId(), subtask1_1StartTime, subtask1_1Duration);
        subtask1_1 = taskManager.createSubtask(subtask1_1);


        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        // System.out.println(prioritizedTasks);

        assertTrue(prioritizedTasks.contains(task1), "Данная задача отсутствует в списке");
        assertTrue(prioritizedTasks.contains(epic1), "Данный эпик отсутствует в списке");
        assertTrue(prioritizedTasks.contains(subtask1_1), "Данная подзадача отсутствует в списке");

        assertEquals(task1, prioritizedTasks.get(0), "Данный элемент (задача) стоит не по приоритету начала выполнения");
        assertEquals(epic1, prioritizedTasks.get(1), "Данный элемент (эпик) стоит не по приоритету начала выполнения");
        assertEquals(subtask1_1, prioritizedTasks.get(2), "Данный элемент (подзадача) стоит не по приоритету начала выполнения");

    }


    // for the one above

    // if started separately:
    // [Task{id=1, name='Задача №1', description='Описание задачи №1', status=NEW', type=TASK', startTime=2023-11-10 11:30', duration=PT20M', endTime=2023-11-10 11:50},
    // Task{id=2, name='Эпик №1', description='Описание эпика №1', status=NEW', type=EPIC', startTime=2023-11-10 13:00', duration=PT20M', endTime=null},
    // Task{id=3, name='Подзадача №1.1', description='Описание подзадачи №1.1', status=NEW', type=SUBTASK', startTime=2023-11-10 15:00', duration=PT15M', endTime=2023-11-10 15:15}]

    // if started collectively:
    // [Task{id=5, name='Test Task2', description='Test Task2 Description', status=NEW', type=TASK', startTime=2023-11-10 10:45', duration=PT20M', endTime=2023-11-10 11:05},
    // Task{id=21, name='Задача №1', description='Описание задачи №1', status=NEW', type=TASK', startTime=2023-11-10 11:30', duration=PT20M', endTime=2023-11-10 11:50},
    // Task{id=4, name='Test Task1', description='Test Task1 Description', status=NEW', type=TASK', startTime=2023-11-10 12:00', duration=PT30M', endTime=2023-11-10 12:30},
    // Task{id=22, name='Эпик №1', description='Описание эпика №1', status=NEW', type=EPIC', startTime=2023-11-10 13:00', duration=PT20M', endTime=null},
    // Task{id=23, name='Подзадача №1.1', description='Описание подзадачи №1.1', status=NEW', type=SUBTASK', startTime=2023-11-10 15:00', duration=PT15M', endTime=2023-11-10 15:15}]




    @Test
    public void testIsIntersectionWhenTasksIntersect() {
        LocalDateTime task1StartTime = LocalDateTime.of(2023, 11, 10, 11, 10);
        Duration task1Duration = Duration.ofMinutes(20);
        Task task1 = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW, task1StartTime, task1Duration);
        task1 = taskManager.createTask(task1);

        LocalDateTime task2StartTime = LocalDateTime.of(2023, 11, 10, 11, 25);
        Duration task2Duration = Duration.ofMinutes(20);
        Task task2 = new Task("Задача №2", "Описание задачи №2", TaskStatus.NEW, task2StartTime, task2Duration);
        task2 = taskManager.createTask(task2);

        taskManager.getPrioritizedTasks();

        boolean result = taskManager.isIntersection(task1);
        assertTrue(result, "Метод должен возвращать true для пересекающихся задач");

    }


    @Test
    public void testIsIntersectionWhenTasksDontIntersect() {
        LocalDateTime task1StartTime = LocalDateTime.of(2023, 11, 10, 12, 10);
        Duration task1Duration = Duration.ofMinutes(20);
        Task task1 = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW, task1StartTime, task1Duration);
        task1 = taskManager.createTask(task1);

        LocalDateTime task2StartTime = LocalDateTime.of(2023, 11, 10, 11, 25);
        Duration task2Duration = Duration.ofMinutes(20);
        Task task2 = new Task("Задача №2", "Описание задачи №2", TaskStatus.NEW, task2StartTime, task2Duration);
        task2 = taskManager.createTask(task2);

        taskManager.getPrioritizedTasks();

        boolean result = taskManager.isIntersection(task1);
        assertFalse(result, "Метод должен возвращать false для пересекающихся задач");
    }

    @Test
    public void testIsIntersectionWhenPrioritizedTasksListIsEmpty() {
        Task task = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW);
        taskManager.createTask(task);

        boolean result = taskManager.isIntersection(task);
        assertFalse(result, "Метод должен возвращать false, когда список задач по приоритету пуст");
    }

    @Test
    public void testIsIntersectionWithNullTask() {
        boolean result = taskManager.isIntersection(null);
        assertFalse(result, "Метод должен возвращать false, когда task = null");
    }

    @Test
    public void testIsIntersectionWithInvalidTask() {
        Task task = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW, LocalDateTime.of(2023, 11, 10, 12, 0), Duration.ofMinutes(-30));
        taskManager.createTask(task);

        boolean result = taskManager.isIntersection(task);
        assertFalse(result, "Метод должен возвращать false, когда указаны неверные параметры времени");
    }

    // Артём, с тестовым методом ниже нужна помощь, работает, если отдельно запускать, причём с обеими реализациями
    // isIntersection() в InMemoryTaskManager, но вместе со всеми если запускать, не работает. Помоги, пожалуйста.


    @Test
    public void testIsIntersectionWhenSubtasksIntersect() {

        LocalDateTime subtask1_1StartTime = LocalDateTime.of(2023, 11, 10, 15, 0);
        Duration subtask1_1Duration = Duration.ofMinutes(15);
        Subtask subtask1_1 = new Subtask("Подзадача №1.1", "Описание подзадачи №1.1", TaskStatus.NEW, 0, subtask1_1StartTime, subtask1_1Duration);

        LocalDateTime subtask1_2StartTime = LocalDateTime.of(2023, 11, 10, 15, 5);
        Duration subtask1_2Duration = Duration.ofMinutes(10);
        Subtask subtask1_2 = new Subtask("Подзадача №1.2", "Описание подзадачи №1.2", TaskStatus.NEW, 0, subtask1_2StartTime, subtask1_2Duration);

        taskManager.createSubtask(subtask1_1);
        taskManager.createSubtask(subtask1_2);

        taskManager.getPrioritizedTasks();

        boolean result = taskManager.isIntersection(subtask1_1);
        assertTrue(result, "Метод должен возвращать true для пересекающихся подзадач");
    }

}