package finaltask.server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import finaltask.manager.HTTPTaskManager;
import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;
import finaltask.tasks.TaskStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

public class HTTPTaskServerTest {

    // порядок действий:
    // kvserverstarter on
    // httptaskservertest on

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static HTTPTaskServer server;
    private static final int PORT = 8082;
    private static final String SERVER_URL = "http://localhost:" + PORT;
    private static HTTPTaskManager httpTaskManager;


    @BeforeAll
    public static void setUp() throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        server = new HTTPTaskServer();
        server.start();
        httpTaskManager = (HTTPTaskManager) server.getManager();
    }

    @AfterAll
    public static void tearDown() {
        server.stop(0);
    }

//
//    @BeforeEach
//    public void init() throws IOException {
//        server = new HTTPTaskServer();
//        server.start();
//        httpTaskManager = (HTTPTaskManager) server.getManager();
//    }

    @AfterEach
    public void cleanUp() {
        server.stop(0);
        httpTaskManager.removeAllTasks();
        httpTaskManager.removeAllEpics();
        httpTaskManager.removeAllSubtasks();
    }


    @Test
    public void testGetTaskByIdEndpoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();


        LocalDateTime task1StartTime = LocalDateTime.of(2023, 11, 10, 12, 30);
        Duration task1Duration = Duration.ofMinutes(20);
        Task task1 = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW, task1StartTime, task1Duration);
        Task createdTask = httpTaskManager.createTask(task1);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/task/?id=1"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request); // отладка


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body()); // отладка
        assertEquals(200, response.statusCode());

        String expectedTask = "{\"task\":{\"id\":1,\"name\":\"Задача №1\",\"description\":\"Описание задачи №1\",\"status\":\"NEW\",\"type\":\"TASK\",\"startTime\":{\"date\":\"2023-11-10\",\"time\":\"12:30\"},\"duration\":{\"minutes\":20},\"endTime\":{\"date\":\"2023-11-10\",\"time\":\"12:50\"}}}";

        assertEquals(expectedTask, response.body());
    }

    @Test
    public void testGetEpicByIdEndpoint() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        LocalDateTime epic2StartTime = LocalDateTime.of(2023, 11, 10, 20, 0);
        Duration epic2Duration = Duration.ofMinutes(20);
        Epic epic2 = new Epic("Эпик №1", "Описание эпика №1", TaskStatus.NEW, epic2StartTime, epic2Duration);
        Epic createdEpic = httpTaskManager.createEpic(epic2);

        httpTaskManager.calculateEpicStartTime(epic2.getId());
        httpTaskManager.calculateEpicDuration(epic2.getId());
        httpTaskManager.calculateEpicEndTime(epic2.getId());


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/epic/?id=1"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request); // отладка

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body()); // отладка
        assertEquals(200, response.statusCode());

        String expectedEpic = "{\"epic\":{\"subtaskIDs\":[],\"id\":1,\"name\":\"Эпик №1\",\"description\":\"Описание эпика №1\",\"status\":\"NEW\",\"type\":\"EPIC\",\"startTime\":{\"date\":\"2023-11-10\",\"time\":\"20:00\"},\"duration\":{\"minutes\":20},\"endTime\":{\"date\":\"2023-11-10\",\"time\":\"20:20\"}}}";

        assertEquals(expectedEpic, response.body());
    }


    @Test
    public void testGetSubtaskByIdEndpoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();


//        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1", TaskStatus.NEW);
//        Epic createdEpic1 = httpTaskManager.createEpic(epic1);


        LocalDateTime subtask1_1StartTime = LocalDateTime.of(2023, 11, 10, 15, 0);
        Duration subtask1_1Duration = Duration.ofMinutes(15);
        Subtask subtask1_1 = new Subtask("Подзадача №1.1", "Описание подзадачи №1.1", TaskStatus.NEW, 0, subtask1_1StartTime, subtask1_1Duration);
        Subtask createdSubtask1_1 = httpTaskManager.createSubtask(subtask1_1);

//        httpTaskManager.calculateEpicStartTime(epic1.getId());
//        httpTaskManager.calculateEpicDuration(epic1.getId());
//        httpTaskManager.calculateEpicEndTime(epic1.getId());


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/subtask/?id=1"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request); // отладка


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body()); // отладка
        assertEquals(200, response.statusCode());

        String expectedSubtask = "{\"subtask\":{\"epicID\":0,\"id\":1,\"name\":\"Подзадача №1.1\",\"description\":\"Описание подзадачи №1.1\",\"status\":\"NEW\",\"type\":\"SUBTASK\",\"startTime\":{\"date\":\"2023-11-10\",\"time\":\"15:00\"},\"duration\":{\"minutes\":15},\"endTime\":{\"date\":\"2023-11-10\",\"time\":\"15:15\"}}}";

        assertEquals(expectedSubtask, response.body());
    }


    @Test
    public void testGetTasksEndpoint() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();


        LocalDateTime task1StartTime = LocalDateTime.of(2023, 11, 10, 12, 30);
        Duration task1Duration = Duration.ofMinutes(20);
        Task task1 = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW, task1StartTime, task1Duration);
        Task createdTask1 = httpTaskManager.createTask(task1);

        LocalDateTime task2StartTime = LocalDateTime.of(2023, 11, 10, 13, 30);
        Duration task2Duration = Duration.ofMinutes(20);
        Task task2 = new Task("Задача №2", "Описание задачи №2", TaskStatus.NEW, task2StartTime, task2Duration);
        Task createdTask2 = httpTaskManager.createTask(task2);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/task/"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request); // отладка

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body()); // отладка
        assertEquals(200, response.statusCode());

        String expectedTask = "{\"tasks\":[{\"id\":1,\"name\":\"Задача №1\",\"description\":\"Описание задачи №1\",\"status\":\"NEW\",\"type\":\"TASK\",\"startTime\":{\"date\":\"2023-11-10\",\"time\":\"12:30\"},\"duration\":{\"minutes\":20},\"endTime\":{\"date\":\"2023-11-10\",\"time\":\"12:50\"}},{\"id\":2,\"name\":\"Задача №2\",\"description\":\"Описание задачи №2\",\"status\":\"NEW\",\"type\":\"TASK\",\"startTime\":{\"date\":\"2023-11-10\",\"time\":\"13:30\"},\"duration\":{\"minutes\":20},\"endTime\":{\"date\":\"2023-11-10\",\"time\":\"13:50\"}}]}";

        assertEquals(expectedTask, response.body());
    }


    @Test
    public void testGetEpicsEndpoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        LocalDateTime epic1StartTime = LocalDateTime.of(2023, 11, 10, 20, 0);
        Duration epic1Duration = Duration.ofMinutes(20);
        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1", TaskStatus.NEW, epic1StartTime, epic1Duration);
        Epic createdEpic1 = httpTaskManager.createEpic(epic1);

        httpTaskManager.calculateEpicStartTime(epic1.getId());
        httpTaskManager.calculateEpicDuration(epic1.getId());
        httpTaskManager.calculateEpicEndTime(epic1.getId());

        LocalDateTime epic2StartTime = LocalDateTime.of(2023, 11, 10, 21, 0);
        Duration epic2Duration = Duration.ofMinutes(20);
        Epic epic2 = new Epic("Эпик №2", "Описание эпика №2", TaskStatus.NEW, epic2StartTime, epic2Duration);
        Epic createdEpic2 = httpTaskManager.createEpic(epic2);

        httpTaskManager.calculateEpicStartTime(epic2.getId());
        httpTaskManager.calculateEpicDuration(epic2.getId());
        httpTaskManager.calculateEpicEndTime(epic2.getId());


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/epic/"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request); // отладка


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body()); // отладка
        assertEquals(200, response.statusCode());

        String expectedEpic = "{\"epics\":[{\"subtaskIDs\":[],\"id\":1,\"name\":\"Эпик №1\",\"description\":\"Описание эпика №1\",\"status\":\"NEW\",\"type\":\"EPIC\",\"startTime\":{\"date\":\"2023-11-10\",\"time\":\"20:00\"},\"duration\":{\"minutes\":20},\"endTime\":{\"date\":\"2023-11-10\",\"time\":\"20:20\"}},{\"subtaskIDs\":[],\"id\":2,\"name\":\"Эпик №2\",\"description\":\"Описание эпика №2\",\"status\":\"NEW\",\"type\":\"EPIC\",\"startTime\":{\"date\":\"2023-11-10\",\"time\":\"21:00\"},\"duration\":{\"minutes\":20},\"endTime\":{\"date\":\"2023-11-10\",\"time\":\"21:20\"}}]}";

        assertEquals(expectedEpic, response.body());
    }


    @Test
    public void testGetSubtasksEndpoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        LocalDateTime subtask1_1StartTime = LocalDateTime.of(2023, 11, 10, 15, 0);
        Duration subtask1_1Duration = Duration.ofMinutes(15);
        Subtask subtask1_1 = new Subtask("Подзадача №1.1", "Описание подзадачи №1.1", TaskStatus.NEW, 0, subtask1_1StartTime, subtask1_1Duration);
        Subtask createdSubtask1_1 = httpTaskManager.createSubtask(subtask1_1);

        LocalDateTime subtask1_2StartTime = LocalDateTime.of(2023, 11, 10, 15, 30);
        Duration subtask1_2Duration = Duration.ofMinutes(10);
        Subtask subtask1_2 = new Subtask("Подзадача №1.2", "Описание подзадачи №1.2", TaskStatus.NEW, 0, subtask1_2StartTime, subtask1_2Duration);
        Subtask createdSubtask1_2 = httpTaskManager.createSubtask(subtask1_2);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/subtask/"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request); // отладка


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body()); // отладка
        assertEquals(200, response.statusCode());

        String expectedSubtask = "{\"subtasks\":[{\"epicID\":0,\"id\":1,\"name\":\"Подзадача №1.1\",\"description\":\"Описание подзадачи №1.1\",\"status\":\"NEW\",\"type\":\"SUBTASK\",\"startTime\":{\"date\":\"2023-11-10\",\"time\":\"15:00\"},\"duration\":{\"minutes\":15},\"endTime\":{\"date\":\"2023-11-10\",\"time\":\"15:15\"}},{\"epicID\":0,\"id\":2,\"name\":\"Подзадача №1.2\",\"description\":\"Описание подзадачи №1.2\",\"status\":\"NEW\",\"type\":\"SUBTASK\",\"startTime\":{\"date\":\"2023-11-10\",\"time\":\"15:30\"},\"duration\":{\"minutes\":10},\"endTime\":{\"date\":\"2023-11-10\",\"time\":\"15:40\"}}]}";

        assertEquals(expectedSubtask, response.body());
    }


    @Test
    public void testDeleteTaskByIdEndpoint() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();


        LocalDateTime task1StartTime = LocalDateTime.of(2023, 11, 10, 12, 30);
        Duration task1Duration = Duration.ofMinutes(20);
        Task task = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW, task1StartTime, task1Duration);
        Task createdTask = httpTaskManager.createTask(task);

        httpTaskManager.removeTaskByID(createdTask.getId());


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/task/?id=1"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());


        Task removedTask = httpTaskManager.getTaskByID(createdTask.getId());
        assertNull(removedTask, "Задача с таким ID не была удалена");

        String expectedTask = "{\"error\":\"Задача не найдена\"}";
        assertEquals(expectedTask, response.body(), "Задача с таким ID не была удалена");
    }

    @Test
    public void testDeleteEpicByIdEndpoint() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();


        LocalDateTime epic2StartTime = LocalDateTime.of(2023, 11, 10, 20, 0);
        Duration epic2Duration = Duration.ofMinutes(20);
        Epic epic2 = new Epic("Эпик №2", "Описание эпика №2", TaskStatus.NEW, epic2StartTime, epic2Duration);
        Epic createdEpic = httpTaskManager.createEpic(epic2);

        httpTaskManager.calculateEpicStartTime(epic2.getId());
        httpTaskManager.calculateEpicDuration(epic2.getId());
        httpTaskManager.calculateEpicEndTime(epic2.getId());


        httpTaskManager.removeEpicByID(createdEpic.getId());


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/epic/?id=1"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());


        Epic removedEpic = httpTaskManager.getEpicByID(createdEpic.getId());
        assertNull(removedEpic, "Эпик с таким ID не был удалён");


        String expectedEpic = "{\"error\":\"Задача не найдена\"}";
        assertEquals(expectedEpic, response.body(), "Эпик с таким ID не был удалён");


        List<Subtask> subtasks = httpTaskManager.getAllSubtasks();
        for (Subtask subtask : subtasks) {
            assertNotEquals(createdEpic.getId(), subtask.getEpicID(), "Все подзадачи, связанные с данным эпиком, не были удалены");
        }

        // сюда надо будет ещё вернуться, чтобы добавить подзадачи, тем самым показав, что подзадачи,
        // связанные с определённым эпиком, тоже удаляются вслед за этим самым эпиком

    }


    @Test
    public void testDeleteSubtaskByIdEndpoint() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();


        LocalDateTime subtask1_1StartTime = LocalDateTime.of(2023, 11, 10, 15, 0);
        Duration subtask1_1Duration = Duration.ofMinutes(15);
        Subtask subtask1_1 = new Subtask("Подзадача №1.1", "Описание подзадачи №1.1", TaskStatus.NEW, 0, subtask1_1StartTime, subtask1_1Duration);
        Subtask createdSubtask1_1 = httpTaskManager.createSubtask(subtask1_1);

        httpTaskManager.removeSubtaskByID(createdSubtask1_1.getId());


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/subtask/?id=1"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());


        Subtask removedSubtask = httpTaskManager.getSubtaskByID(createdSubtask1_1.getId());
        assertNull(removedSubtask, "Подзадача с таким ID не была удалена");

        String expectedSubtask = "{\"error\":\"Задача не найдена\"}";
        assertEquals(expectedSubtask, response.body(), "Подзадача с таким ID не была удалена");

    }


    @Test
    public void testDeleteTasksEndpoint() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        LocalDateTime task1StartTime = LocalDateTime.of(2023, 11, 10, 12, 30);
        Duration task1Duration = Duration.ofMinutes(20);
        Task task1 = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW, task1StartTime, task1Duration);
        Task createdTask1 = httpTaskManager.createTask(task1);

        LocalDateTime task2StartTime = LocalDateTime.of(2023, 11, 10, 12, 0);
        Duration task2Duration = Duration.ofMinutes(30);
        Task task2 = new Task("Задача №2", "Описание задачи №2", TaskStatus.IN_PROGRESS, task2StartTime, task2Duration);
        Task createdTask2 = httpTaskManager.createTask(task2);

        httpTaskManager.removeAllTasks();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/task/"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());


        List<Task> tasks = httpTaskManager.getAllTasks();
        assertEquals(0, tasks.size(), "Все задачи не были удалены");


        String expectedTask = "{\"tasks\":[]}";
        assertEquals(expectedTask, response.body(), "Все задачи не были удалены");

    }


    @Test
    public void testDeleteEpicsEndpoint() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

//        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1", TaskStatus.NEW);
//        epic1 = httpTaskManager.createEpic(epic1);
//
//
//        LocalDateTime subtask1_1StartTime = LocalDateTime.of(2023, 11, 10, 15, 0);
//        Duration subtask1_1Duration = Duration.ofMinutes(15);
//        Subtask subtask1_1 = new Subtask("Подзадача №1.1", "Описание подзадачи №1.1", TaskStatus.NEW, epic1.getId(), subtask1_1StartTime, subtask1_1Duration);
//
//        LocalDateTime subtask1_2StartTime = LocalDateTime.of(2023, 11, 10, 15, 30);
//        Duration subtask1_2Duration = Duration.ofMinutes(10);
//        Subtask subtask1_2 = new Subtask("Подзадача №1.2", "Описание подзадачи №1.2", TaskStatus.NEW, epic1.getId(), subtask1_2StartTime, subtask1_2Duration);
//
//        LocalDateTime subtask1_3StartTime = LocalDateTime.of(2023, 11, 10, 16, 0);
//        Duration subtask1_3Duration = Duration.ofMinutes(25);
//        Subtask subtask1_3 = new Subtask("Подзадача №1.3", "Описание подзадачи №1.3", TaskStatus.NEW, epic1.getId(), subtask1_3StartTime, subtask1_3Duration);
//
//        subtask1_1 = httpTaskManager.createSubtask(subtask1_1);
//        subtask1_2 = httpTaskManager.createSubtask(subtask1_2);
//        subtask1_3 = httpTaskManager.createSubtask(subtask1_3);
//
//        httpTaskManager.calculateEpicStartTime(epic1.getId());
//        httpTaskManager.calculateEpicDuration(epic1.getId());
//        httpTaskManager.calculateEpicEndTime(epic1.getId());


        LocalDateTime epic1StartTime = LocalDateTime.of(2023, 11, 10, 20, 0);
        Duration epic1Duration = Duration.ofMinutes(20);
        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1", TaskStatus.NEW, epic1StartTime, epic1Duration);
        Epic createdEpic1 = httpTaskManager.createEpic(epic1);

        httpTaskManager.calculateEpicStartTime(epic1.getId());
        httpTaskManager.calculateEpicDuration(epic1.getId());
        httpTaskManager.calculateEpicEndTime(epic1.getId());

        LocalDateTime epic2StartTime = LocalDateTime.of(2023, 11, 10, 21, 0);
        Duration epic2Duration = Duration.ofMinutes(20);
        Epic epic2 = new Epic("Эпик №2", "Описание эпика №2", TaskStatus.NEW, epic2StartTime, epic2Duration);
        Epic createdEpic2 = httpTaskManager.createEpic(epic2);

        httpTaskManager.calculateEpicStartTime(epic2.getId());
        httpTaskManager.calculateEpicDuration(epic2.getId());
        httpTaskManager.calculateEpicEndTime(epic2.getId());


        httpTaskManager.removeAllEpics();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/epic/"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());


        List<Epic> epics = httpTaskManager.getAllEpics();
        assertEquals(0, epics.size(), "Все эпики не были удалены");

//        List<Subtask> subtasks = httpTaskManager.getAllSubtasks();
//        assertEquals(0, subtasks.size(), "Все подзадачи, связанные со всеми эпиками не были удалены");

        String expectedEpic = "{\"epics\":[]}";
        assertEquals(expectedEpic, response.body(), "Все эпики не были удалены");

        // сюда надо будет ещё вернуться, чтобы добавить подзадачи, тем самым показав, что подзадачи,
        // связанные с определённым эпиком, тоже удаляются вслед за этим самым эпиком

    }


    @Test
    public void testDeleteSubtasksEndpoint() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        LocalDateTime subtask1_1StartTime = LocalDateTime.of(2023, 11, 10, 15, 0);
        Duration subtask1_1Duration = Duration.ofMinutes(15);
        Subtask subtask1_1 = new Subtask("Подзадача №1.1", "Описание подзадачи №1.1", TaskStatus.NEW, 0, subtask1_1StartTime, subtask1_1Duration);
        Subtask createdSubtask1_1 = httpTaskManager.createSubtask(subtask1_1);

        LocalDateTime subtask1_2StartTime = LocalDateTime.of(2023, 11, 10, 16, 0);
        Duration subtask1_2Duration = Duration.ofMinutes(15);
        Subtask subtask1_2 = new Subtask("Подзадача №1.2", "Описание подзадачи №1.2", TaskStatus.NEW, 0, subtask1_2StartTime, subtask1_2Duration);
        Subtask createdSubtask1_2 = httpTaskManager.createSubtask(subtask1_2);

        httpTaskManager.removeAllSubtasks();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/subtask/"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());


        List<Subtask> subtasks = httpTaskManager.getAllSubtasks();
        assertEquals(0, subtasks.size(), "Все подзадачи не были удалены");


        String expectedEpic = "{\"subtasks\":[]}";
        assertEquals(expectedEpic, response.body(), "Все подзадачи не были удалены");

    }


    @Test
    public void testCreateTaskEndpoint() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        LocalDateTime taskStartTime = LocalDateTime.of(2023, 11, 10, 12, 30);
        Duration taskDuration = Duration.ofMinutes(20);
        Task task = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW, taskStartTime, taskDuration);
        Task createdTask = httpTaskManager.createTask(task);

        assertNotNull(createdTask, "Задача не найдена.");
        assertEquals(task, createdTask, "Задачи не совпадают.");
        assertNotNull(createdTask.getId(), "Идентификатор задачи не должен быть null");


        List<Task> allTasks = httpTaskManager.getAllTasks();
        assertEquals(1, allTasks.size(), "Количество задач после создания не совпадает");


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/task/"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());

        // Gson gson = new Gson();

//        List<Task> tasksList = new ArrayList<>();
//        tasksList.add(createdTask);
//
//        Gson gson = new GsonBuilder()
//                .setPrettyPrinting()
//                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(formatter.format(src)))
//                .registerTypeAdapter(Duration.class, (JsonSerializer<Duration>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toMinutes()))
//                .create();

//        HttpExchange httpExchange;
//        String jsonRequest = readText(httpExchange);
//        JsonObject jsonObject = JsonParser.parseString(jsonRequest).getAsJsonObject();


//        JsonObject newResponse = new JsonObject();


//        Task newResponse = gson.fromJson(response.body(), Task.class);

        // String jsonTask = gson.toJson(getAllTasks());



        // System.out.println(response.body());

        // String jsonCreatedTask = gson.toJson(createdTask);

//        String jsonTasksList = gson.toJson(tasksList);
//
//        String jsonTasksListNew = "{\"tasks\":" + jsonTasksList + "}";
//
//        JsonObject jsonObject = JsonParser.parseString(jsonTasksListNew).getAsJsonObject();


        // assertEquals(createdTask.toString(), newResponse.toString(), "Задачи не совпадают.");
        // assertEquals(jsonCreatedTask, response.body(), "Задачи не совпадают.");
        // assertEquals(jsonTasksListNew, response.body(), "Задачи не совпадают.");
        // assertEquals(jsonObject, response.body(), "Задачи не совпадают.");
        // assertEquals(jsonObject, adjustedActual, "Задачи не совпадают.");

        String expectedTask = "{\"tasks\":[{\"id\":1,\"name\":\"Задача №1\",\"description\":\"Описание задачи №1\",\"status\":\"NEW\",\"type\":\"TASK\",\"startTime\":{\"date\":\"2023-11-10\",\"time\":\"12:30\"},\"duration\":{\"minutes\":20},\"endTime\":{\"date\":\"2023-11-10\",\"time\":\"12:50\"}}]}";
        assertEquals(expectedTask, response.body(), "Задачи не совпадают");


    }


}

/*
    @Test
    public void testCreateEpicEndpoint() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        Epic newEpic = new Epic("Test New Epic", "Test New Epic Description");
        Epic createdEpic = httpTaskManager.createEpic(newEpic);
        assertNotNull(createdEpic, "Эпик не найден.");
        assertEquals(newEpic, createdEpic, "Эпики не совпадают.");
        assertNotNull(createdEpic.getId(), "Идентификатор эпика не должен быть null");

        List<Epic> allEpics = httpTaskManager.getAllEpics();
        assertEquals(1, allEpics.size(), "Количество эпиков после создания не совпадает");


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/epic"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());

        assertEquals(createdEpic.toString(), response.body());


    }

    @Test
    public void testCreateSubtaskEndpoint() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();


        Subtask newSubtask = new Subtask("Test New Subtask", "Test New Subtask Description", TaskStatus.NEW, 0);
        Subtask createdSubtask = httpTaskManager.createSubtask(newSubtask);
        assertNotNull(createdSubtask, "Подзадача не найдена.");
        assertEquals(newSubtask, createdSubtask, "Подзадачи не совпадают.");
        assertNotNull(createdSubtask.getId(), "Идентификатор подзадачи не должен быть null");


        List<Subtask> allSubtasks = httpTaskManager.getAllSubtasks();
        assertEquals(1, allSubtasks.size(), "Количество подзадача после создания не совпадает");


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/subtask"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());

        assertEquals(createdSubtask.toString(), response.body());


    }


    @Test
    public void testHistoryEndpoint() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();






        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/history"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());


    }



//
//    @Test
//    public void testDefaultEndpoint() {

//    }
//
//    @Test
//    public void testEpicSubtasksEndpoint() {

//    }


}


     */