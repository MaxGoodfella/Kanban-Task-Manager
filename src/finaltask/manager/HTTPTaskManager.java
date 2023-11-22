package finaltask.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import finaltask.client.KVClient;
import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTaskManager {

    private static final String TASKS_KEY = "tasks";
    private static final String EPICS_KEY = "epics";
    private static final String SUBTASKS_KEY = "subtasks";
    private static final String HISTORY_KEY = "history";


    private KVClient client;
    private Gson gson;


    public HTTPTaskManager(String serverURL) {
        super(null, new InMemoryHistoryManager());
        this.client = new KVClient(serverURL);
        this.gson = new Gson();
    }


    @Override
    public void save() {

        try {
            String jsonTask = gson.toJson(getAllTasks());
            client.put(TASKS_KEY, jsonTask);

            String jsonEpic = gson.toJson(getAllEpics());
            client.put(EPICS_KEY, jsonEpic);

            String jsonSubtask = gson.toJson(getAllSubtasks());
            client.put(SUBTASKS_KEY, jsonSubtask);

            List<Integer> historyIDs = historyManager.getHistory().stream()
                    .map(it -> it.getId())
                    .collect(Collectors.toList());
            String jsonHistoryIDs = gson.toJson(historyIDs);
            client.put(HISTORY_KEY, jsonHistoryIDs);

            System.out.println("Задачи сохранены на сервере");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Произошла ошибка при сохранении данных на сервере");
        }

    }


    public void loadFromServer() {

        String jsonTasks = client.load(TASKS_KEY);
        String jsonEpics = client.load(EPICS_KEY);
        String jsonSubtasks = client.load(SUBTASKS_KEY);
        String jsonHistory = client.load(HISTORY_KEY);

        Type taskListType = new TypeToken<List<Task>>() {}.getType();
        Type epicListType = new TypeToken<List<Epic>>() {}.getType();
        Type subtaskListType = new TypeToken<List<Subtask>>() {}.getType();


        List<Task> tasks = gson.fromJson(jsonTasks, taskListType);
        List<Epic> epics = gson.fromJson(jsonEpics, epicListType);
        List<Subtask> subtasks = gson.fromJson(jsonSubtasks, subtaskListType);

        for (Task task : tasks) {
            taskStorage.put(task.getId(), task);
        }
        for (Epic epic : epics) {
            epicStorage.put(epic.getId(), epic);
        }
        for (Subtask subtask : subtasks) {
            subtaskStorage.put(subtask.getId(), subtask);
        }


        List<Integer> historyIDs = gson.fromJson(jsonHistory, List.class);
        historyManager.setHistory(historyIDs);

    }

}