package finaltask.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {
    private static final String URL = "http://localhost:8078/";

    private String apiToken;
    private HttpClient httpClient;

    public KVClient(String URL) {
        apiToken = register();
    }

    public String load(String key) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                return null;
            }
            if (response.statusCode() != 200) {
                throw new RuntimeException("Плохой ответ, не 200, а: " + response.statusCode());
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Не получается сделать запрос");
        }
    }

    public void put(String key, String value) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Плохой ответ, не 200, а: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Не получается сделать запрос");
        }
    }

    private String register() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "register"))
                    .GET()
                    .build();

            httpClient = HttpClient.newHttpClient();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Плохой ответ, не 200, а: " + response.statusCode());
            }

            apiToken = response.body();
            return apiToken;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Не получается сделать запрос");
        }
    }


    public static void main(String[] args) {

        // демонстрация работы

        KVClient kvClient = new KVClient("http://localhost:8078/");

        String[] keys = {"tasks", "epics", "subtasks", "history"};


        String taskValue = "{\"id\":1, \"type\":\"TASK\", \"name\":\"Задача №1\", \"status\":\"NEW\", \"description\":\"Описание задачи №1\", \"startTime\":\"2023-11-10 12:30\", \"duration\":\"PT20M\", \"endTime\":\"2023-11-10 12:50\"}";
        String taskValueUpdated = "{\"id\":1, \"type\":\"TASK\", \"name\":\"Задача №1\", \"status\":\"DONE\", \"description\":\"Описание задачи №1\", \"startTime\":\"2023-11-10 12:30\", \"duration\":\"PT20M\", \"endTime\":\"2023-11-10 12:50\"}";

        kvClient.register();
        kvClient.put(keys[0], taskValue);
        System.out.println( kvClient.load(keys[0]));

        kvClient.put(keys[0], taskValueUpdated);
        System.out.println( kvClient.load(keys[0]));



        String epicValue = "{\"id\":1, \"type\":\"EPIC\", \"name\":\"Эпик №1\", \"status\":\"NEW\", \"description\":\"Описание эпика №1\", \"startTime\":\"2023-11-10 12:30\", \"duration\":\"PT20M\", \"endTime\":\"2023-11-10 12:50\"}";
        String epicValueUpdated = "{\"id\":1, \"type\":\"EPIC\", \"name\":\"Эпик №1\", \"status\":\"DONE\", \"description\":\"Описание эпика №1\", \"startTime\":\"2023-11-10 12:30\", \"duration\":\"PT20M\", \"endTime\":\"2023-11-10 12:50\"}";

        kvClient.register();
        kvClient.put(keys[1], epicValue);
        System.out.println( kvClient.load(keys[1]));

        kvClient.put(keys[1], epicValueUpdated);
        System.out.println( kvClient.load(keys[1]));

        // с остальными ключами - аналогично


    }

}

