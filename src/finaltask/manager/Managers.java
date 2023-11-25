package finaltask.manager;

import com.google.gson.*;

import java.io.File;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Managers {

    private Managers() {}

    private static final HistoryManager defaultHistoryManager = new InMemoryHistoryManager();
    private static final InMemoryTaskManager defaultTaskManager = new InMemoryTaskManager(defaultHistoryManager);
    private static final FileBackedTaskManager defaultFileBackedTaskManager = new FileBackedTaskManager(new File("/Users/MaximGuseynov/dev3/sprint8/java-kanban/src/resources/sprint7/test.txt"), defaultHistoryManager);

    private static final HTTPTaskManager defaultHttpTaskManager = new HTTPTaskManager("http://localhost:8078/");


    public static HTTPTaskManager getDefault() {
        return defaultHttpTaskManager;
    }

    public static HistoryManager getHistoryDefault() {
        return defaultHistoryManager;
    }

    public static FileBackedTaskManager getDefaultFileBackedTaskManager() {
        return defaultFileBackedTaskManager;
    }

    public static Gson getDefaultGson() {
        return new GsonBuilder()
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

}