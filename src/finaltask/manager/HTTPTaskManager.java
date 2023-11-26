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
    private static final String GENERATED_ID_KEY = "generatedID";

    private final KVClient client;
    private final Gson gson;

    // private int generatedID;


    public HTTPTaskManager(String serverURL) {
        this(serverURL, false); // если false loadFromFile() не вызывается, если true - вызывается
    }

    public HTTPTaskManager(String serverURL, boolean loadFromServer) {
        super(null, new InMemoryHistoryManager());
        this.client = new KVClient();
        this.gson = new Gson(); // здесь решил оставить как есть, так как способ ниже начинает мне крашить тест)
        // this.gson = Managers.getDefaultGson();

        // this.generatedID = initialGeneratedID;

        if (loadFromServer) {
            loadFromServer();
        }
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void save() {

        String jsonTask = gson.toJson(getAllTasks());
        client.put(TASKS_KEY, jsonTask);

        String jsonEpic = gson.toJson(getAllEpics());
        client.put(EPICS_KEY, jsonEpic);

        String jsonSubtask = gson.toJson(getAllSubtasks());
        client.put(SUBTASKS_KEY, jsonSubtask);

        List<Integer> historyIDs = historyManager.getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        String jsonHistoryIDs = gson.toJson(historyIDs);
        client.put(HISTORY_KEY, jsonHistoryIDs);

        // client.put(GENERATED_ID_KEY, String.valueOf(FileBackedTaskManager.getGeneratedID()));

//        generatedID = getAllTasks().stream()
//                .mapToInt(Task::getId)
//                .max()
//                .orElse(0);

        client.put(GENERATED_ID_KEY, String.valueOf(generatedID));

        System.out.println("Задачи сохранены на сервере");

    }


    private void loadFromServer() {

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


        // List<Integer> historyIDs = gson.fromJson(jsonHistory, List.class);
        // historyManager.setHistory(historyIDs);

        List<Integer> historyIDs = gson.fromJson(jsonHistory, new TypeToken<List<Integer>>() {}.getType());


        for (Integer taskId : historyIDs) {
            if (taskStorage.containsKey(taskId)) {
                historyManager.addTask(taskStorage.get(taskId));
            } else if (epicStorage.containsKey(taskId)) {
                historyManager.addTask(epicStorage.get(taskId));
            } else if (subtaskStorage.containsKey(taskId)) {
                historyManager.addTask(subtaskStorage.get(taskId));
            }
        }



//        generatedID = tasks.stream()
//                .mapToInt(Task::getId)
//                .max()
//                .orElse(0);

        String jsonGeneratedID = client.load(GENERATED_ID_KEY);
        super.generatedID = Integer.parseInt(jsonGeneratedID);

    }

}