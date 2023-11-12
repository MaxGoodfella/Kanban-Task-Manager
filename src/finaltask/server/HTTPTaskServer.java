package finaltask.server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import finaltask.manager.Managers;
import finaltask.manager.TaskManager;
import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HTTPTaskServer {

    public static final int PORT = 8082;

    private HttpServer server;

    private TaskManager manager;

    // private HTTPTaskManager manager;

    private Gson gson;

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public HTTPTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/task/", this::handler);
        server.createContext("/tasks/epic/", this::handler);
        server.createContext("/tasks/subtask/", this::handler);
        server.createContext("/tasks/history/", this::handler);
        server.createContext("/tasks/", this::handler);
        server.createContext("/tasks/subtask/epic/", this::handler);



        this.manager = Managers.getDefault();


        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                    @Override
                    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("date", localDateTime.toLocalDate().toString());
                        jsonObject.addProperty("time", localDateTime.toLocalTime().toString());
                        return jsonObject;
                    }
                })
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        String dateTimeString = json.getAsString();
                        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    }
                })
                .registerTypeAdapter(Duration.class, new JsonSerializer<Duration>() {
                    @Override
                    public JsonElement serialize(Duration duration, Type type, JsonSerializationContext jsonSerializationContext) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("minutes", duration.toMinutes());
                        return jsonObject;
                    }
                })
                .registerTypeAdapter(Duration.class, new JsonDeserializer<Duration>() {
                    @Override
                    public Duration deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        String durationString = jsonElement.getAsString();
                        return Duration.parse(durationString);
                    }
                })
                .create();


    }



    private void handler(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String response;
        String jsonRequest = readText(httpExchange);
        String requestURI;

        switch (method) {

            case "GET":

                JsonObject jsonResponse = new JsonObject();
                requestURI = httpExchange.getRequestURI().getPath();

                if (httpExchange.getRequestURI().getQuery() != null && httpExchange.getRequestURI().getQuery().startsWith("id=")) {

                    int taskId = parsePathID(httpExchange.getRequestURI().getQuery());
                    Task task = null;

                    if (requestURI.startsWith("/tasks/task/")) {
                        task = manager.getTaskByID(taskId);
                    } else if (requestURI.startsWith("/tasks/epic/")) {
                        task = manager.getEpicByID(taskId);
                    } else if (requestURI.startsWith("/tasks/subtask/")) {
                        task = manager.getSubtaskByID(taskId);
                    }


                    if (task != null) {
                        if (requestURI.startsWith("/tasks/task/")) {
                            jsonResponse.add("task", gson.toJsonTree(task));
                        } else if (requestURI.startsWith("/tasks/epic/")) {
                            jsonResponse.add("epic", gson.toJsonTree(task));
                        } else if (requestURI.startsWith("/tasks/subtask/")) {
                            jsonResponse.add("subtask", gson.toJsonTree(task));
                        }
                    } else {
                        jsonResponse.addProperty("error", "Задача не найдена");
                    }

                } else {

                    if (requestURI.startsWith("/tasks/subtask/epic/")) {
                        int epicId = parsePathID(requestURI);
                        List<Subtask> subtasks = manager.getEpicSubtasksByEpicID(epicId);
                        jsonResponse.add("subtasks", gson.toJsonTree(subtasks));
                    } else {

                        if (requestURI.equals("/tasks/task/")) {
                            jsonResponse.add("tasks", gson.toJsonTree(manager.getAllTasks()));
                        } else if (requestURI.equals("/tasks/epic/")) {
                            jsonResponse.add("epics", gson.toJsonTree(manager.getAllEpics()));
                        } else if (requestURI.equals("/tasks/subtask/")) {
                            jsonResponse.add("subtasks", gson.toJsonTree(manager.getAllSubtasks()));
                        } else if (requestURI.equals("/tasks/history/")) {
                            jsonResponse.add("history", gson.toJsonTree(manager.getHistory()));
                        } else if (requestURI.equals("/tasks/")) {
                            jsonResponse.add("prioritized_tasks", gson.toJsonTree(manager.getPrioritizedTasks()));
                        } else {
                            jsonResponse.addProperty("error", "Неизвестный запрос");
                        }
                    }
                }

                response = jsonResponse.toString();
                System.out.println(response);
                break;


            case "POST":

                System.out.println("Received JSON request: " + jsonRequest); // отладка

                try {
                    JsonObject jsonObject = JsonParser.parseString(jsonRequest).getAsJsonObject();
                    String type = jsonObject.get("type").getAsString();

                    if (type.equals("TASK")) {

                        Task task = gson.fromJson(jsonObject, Task.class);
                        System.out.println("Parsed Task: " + task); // отладка

                        if (manager.getTaskByID(task.getId()) != null) {
                            manager.updateTask(task);
                            response = "Задача обновлена успешно";
                            System.out.println(response);
                        } else {
                            manager.createTask(task);
                            response = "Задача добавлена успешно";
                            System.out.println(response);
                        }
                    } else if (type.equals("EPIC")) {
                        Epic epic = gson.fromJson(jsonObject, Epic.class);
                        System.out.println("Parsed Epic: " + epic); // отладка

                        if (manager.getEpicByID(epic.getId()) != null) {
                            manager.updateEpic(epic);
                            response = "Эпик обновлён успешно";
                            System.out.println(response);
                        } else {
                            manager.createEpic(epic);
                            response = "Эпик добавлен успешно";
                            System.out.println(response);
                        }
                    } else if (type.equals("SUBTASK")) {
                        Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                        System.out.println("Parsed Subtask: " + subtask); // отладка

                        if (manager.getSubtaskByID(subtask.getId()) != null) {
                            manager.updateSubtask(subtask);
                            response = "Подзадача обновлена успешно";
                            System.out.println(response);
                        } else {
                            manager.createSubtask(subtask);
                            response = "Подзадача добавлена успешно";
                            System.out.println(response);
                        }
                    } else {
                        response = "Неизвестный тип задачи: " + type;
                        System.out.println(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response = "Ошибка при разборе JSON: " + e.getMessage();
                    System.out.println(response);
                }
                break;


            case "DELETE":

                try {

                    requestURI = httpExchange.getRequestURI().getPath();
                    String query = httpExchange.getRequestURI().getQuery();
                    int taskId = -1;

                    if (query != null) {
                        taskId = parsePathID(query);
                    }

                    if (requestURI.startsWith("/tasks/task/") || requestURI.equals("/tasks/task")) {

                        if (taskId != -1) {
                            manager.removeTaskByID(taskId);
                            response = "Задача удалена успешно";
                        } else {
                            manager.removeAllTasks();
                            response = "Задачи удалены успешно";
                        }
                        System.out.println(response);
                    } else if (requestURI.startsWith("/tasks/epic/") || requestURI.equals("/tasks/epic")) {

                        if (taskId != -1) {
                            manager.removeEpicByID(taskId);
                            response = "Эпик удалён успешно";
                        } else {
                            manager.removeAllEpics();
                            response = "Эпики удалёны успешно";
                        }
                        System.out.println(response);
                    } else if (requestURI.startsWith("/tasks/subtask/") || requestURI.equals("/tasks/subtask")) {

                        if (taskId != -1) {
                            manager.removeSubtaskByID(taskId);
                            response = "Подзадача удалена успешно";
                        } else {
                            manager.removeAllSubtasks();
                            response = "Подзадачи удалены успешно";
                        }
                        System.out.println(response);
                    } else {
                        response = "Неизвестный запрос";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response = "Ошибка при удалении: " + e.getMessage();
                    System.out.println(response);
                }
                break;


            default:
                response = "Ожидаем GET/POST/DELETE запросы, а получили " + method;
                System.out.println(response);
                break;

        }


        sendText(httpExchange, response);
    }


    public static void main(String[] args) throws IOException {
        final HTTPTaskServer server = new HTTPTaskServer();
        server.start();

        // server.stop(1);
    }

    private int parsePathID(String path) {

        try {
            String[] parts = path.split("=");
            if (parts.length > 1) {
                return Integer.parseInt(parts[1]);
            }
        } catch (NumberFormatException exception) {
            System.err.println("Invalid id format: " + path);
        }
        return -1;

    }


    public void start() {
        System.out.println("HTTPTaskServer started in " + PORT);
        server.start();
    }

    public void stop(int delay) {
        server.stop(delay);
        System.out.println("HTTPTaskServer has stopped");
    }

    private void sendText(HttpExchange httpExchange, String response) throws IOException {

        httpExchange.getResponseHeaders().add("Content-Type", String.format("application/json; charset=%s", CHARSET));
        byte[] bytes = response.getBytes(Charset.defaultCharset());
        httpExchange.sendResponseHeaders(200, bytes.length);
        httpExchange.getResponseBody().write(bytes);

    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    public TaskManager getManager() {
        return manager;
    }


//    public HTTPTaskManager getManager() {
//        return manager;
//    }
}