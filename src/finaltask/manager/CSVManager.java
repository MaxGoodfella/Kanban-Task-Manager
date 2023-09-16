package finaltask.manager;

import finaltask.tasks.*;

import java.util.ArrayList;
import java.util.List;

public class CSVManager {

    // private CSVManager() {}

    private static final String DELIMITER = ",";

    public String toString(Task task) {

        String result = task.getId() + DELIMITER +
                task.getType() + DELIMITER +
                task.getName() + DELIMITER +
                task.getStatus() + DELIMITER +
                task.getDescription() + DELIMITER;

        if (task.getType() == TaskType.SUBTASK) {
            result = result + ((Subtask) task).getEpicID();
        }
        return result;
    }


    public Task fromString(String taskStr) {

        String[] parts = taskStr.split(DELIMITER);
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        TaskStatus status = TaskStatus.valueOf(parts[3]);
        String description = parts[4];

        Task task;

        switch (type) {
            case TASK:
                task = new Task(name, description, status);
                break;
            case EPIC:
                task = new Epic(name, description, status);
                break;
            case SUBTASK:
                int epicID = Integer.parseInt(parts[5]);
                task = new Subtask(name, description, status, epicID);
                break;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }

        task.setId(id);
        return task;
    }


    public String historyToString(HistoryManager manager) {

        List<String> result = new ArrayList<>();

        List<Task> history = manager.getHistory();

        for (Task task : history) {
            result.add(String.valueOf(task.getId()));
        }

        return String.join(DELIMITER, result);
    }


    public List<Integer> historyFromString(String historyStr) {
        List<Integer> history = new ArrayList<>();
        String[] historyItems = historyStr.split(DELIMITER);
        for (String historyItem : historyItems) {
            int taskId = Integer.parseInt(historyItem);
            history.add(taskId);
        }
        return history;
    }

    public String getHeading() {
        return "id,type,name,status,description,epic";
    }

}
