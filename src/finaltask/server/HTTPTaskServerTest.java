package finaltask.server;

import finaltask.manager.HTTPTaskManager;
import finaltask.manager.KVClient;
import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;
import finaltask.tasks.TaskStatus;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HTTPTaskServerTest {

    // порядок действий:
    // kvserverstarter on
    // httptaskservertest on

    private static HTTPTaskServer server;
    private static final int PORT = 8080;
    private static final String SERVER_URL = "http://localhost:" + PORT;

    HTTPTaskManager httpTaskManager = new HTTPTaskManager(SERVER_URL);



    @BeforeAll
    public static void setUp() throws IOException {
        server = new HTTPTaskServer();
        server.start();
    }

    @AfterAll
    public static void tearDown() {
        //server.stop(0);
    }

    @BeforeEach
    public void init() throws IOException {
//        server = new HTTPTaskServer();
//        server.start();
    }

    @AfterEach
    public void cleanUp() {
//        server.stop(0);
    }


    @Test
    public void testGetTaskByIdEndpoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();


        LocalDateTime task1StartTime = LocalDateTime.of(2023, 11, 10, 12, 30);
        Duration task1Duration = Duration.ofMinutes(20);
        Task task1 = new Task("Задача №1", "Описание задачи №1", TaskStatus.NEW, task1StartTime, task1Duration);
        Task createdTask = httpTaskManager.createTask(task1);

        // httpTaskManager.save();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/task?id=1"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());

        String expectedTask = "{\"id\":1, \"type\":\"TASK\", \"name\":\"Задача №1\", \"status\":\"NEW\", \"description\":\"Описание задачи №1\", \"startTime\":\"2023-11-10 12:30\", \"duration\":\"PT20M\", \"endTime\":\"2023-11-10 12:50\"}";

        assertEquals(expectedTask, response.body());
    }

    @Test
    public void testGetEpicByIdEndpoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/epic?id=1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        String expectedTask = "{\"id\":1, \"type\":\"EPIC\", \"name\":\"Эпик №1\", \"status\":\"NEW\", \"description\":\"Описание эпика №1\", \"startTime\":\"2023-11-10 13:30\", \"duration\":\"PT20M\", \"endTime\":\"2023-11-10 13:50\"}";

        assertEquals(expectedTask, response.body());
    }

    @Test
    public void testGetSubtaskByIdEndpoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/subtask?id=1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        String expectedTask = "{\"epicID\":1, \"id\":1, \"type\":\"SUBTASK\", \"name\":\"Подзадача №1\", \"status\":\"NEW\", \"description\":\"Описание подзадачи №1\", \"startTime\":\"2023-11-10 16:30\", \"duration\":\"PT20M\", \"endTime\":\"2023-11-10 16:50\"}";

        assertEquals(expectedTask, response.body());
    }



    @Test
    public void testGetTasksEndpoint() throws IOException, InterruptedException {

        // вероятно, потом стоит добавить еще одну для демонстрации правильной работы

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/task"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        String expectedTask = "{\"id\":1, \"type\":\"TASK\", \"name\":\"Задача №1\", \"status\":\"NEW\", \"description\":\"Описание задачи №1\", \"startTime\":\"2023-11-10 12:30\", \"duration\":\"PT20M\", \"endTime\":\"2023-11-10 12:50\"}";

        assertEquals(expectedTask, response.body());
    }

    @Test
    public void testGetEpicsEndpoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/epic"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        String expectedTask = "{\"id\":1, \"type\":\"EPIC\", \"name\":\"Эпик №1\", \"status\":\"NEW\", \"description\":\"Описание эпика №1\", \"startTime\":\"2023-11-10 13:30\", \"duration\":\"PT20M\", \"endTime\":\"2023-11-10 13:50\"}";

        assertEquals(expectedTask, response.body());
    }

    @Test
    public void testGetSubtasksEndpoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/subtask"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        String expectedTask = "{\"epicID\":1, \"id\":1, \"type\":\"SUBTASK\", \"name\":\"Подзадача №1\", \"status\":\"NEW\", \"description\":\"Описание подзадачи №1\", \"startTime\":\"2023-11-10 16:30\", \"duration\":\"PT20M\", \"endTime\":\"2023-11-10 16:50\"}";

        assertEquals(expectedTask, response.body());
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
                .uri(URI.create(SERVER_URL + "/tasks/task?id=1"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());


        Task removedTask = httpTaskManager.getTaskByID(createdTask.getId());
        assertNull(removedTask, "Задача с таким ID не была удалена");

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


        httpTaskManager.getEpicByID(createdEpic.getId());


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/tasks/epic?id=1"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());


        Epic removedEpic = httpTaskManager.getEpicByID(createdEpic.getId());
        assertNull(removedEpic, "Эпик с таким ID не был удалён");


        List<Subtask> subtasks = httpTaskManager.getAllSubtasks();
        for (Subtask subtask : subtasks) {
            assertNotEquals(createdEpic.getId(), subtask.getEpicID(), "Все подзадачи, связанные с данным эпиком, не были удалены");
        }

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
                .uri(URI.create(SERVER_URL + "/tasks/subtask?id=1"))
                .GET()
                .build();

        System.out.println("Отправлен запрос: " + request);


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ответ сервера: " + response.body());
        assertEquals(200, response.statusCode());


        Subtask removedSubtask = httpTaskManager.getSubtaskByID(createdSubtask1_1.getId());
        assertNull(removedSubtask, "Подзадача с таким ID не была удалена");

    }



//    @Test
//    public void testHistoryEndpoint() {

//    }
//
//    @Test
//    public void testDefaultEndpoint() {

//    }
//
//    @Test
//    public void testEpicSubtasksEndpoint() {

//    }


}
