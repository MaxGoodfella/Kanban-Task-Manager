package finaltask.manager;

import finaltask.tasks.*;

import java.util.ArrayList;
import java.util.List;

public class CSVManager {


    private static final String DELIMITER = ",";

    public String toString(Task task) {

        String result = task.getId() + DELIMITER +
                task.getType() + DELIMITER +
                task.getName() + DELIMITER +
                task.getStatus() + DELIMITER +
                task.getDescription() + DELIMITER;
        // Артем, видел твой коммент про StringJoiner, но пока не знаю, как его делать, поэтому оставил как есть)


        if (task.getType() == TaskType.SUBTASK) {
            result = result + ((Subtask) task).getEpicID();
        }
        return result;
    }


    public Task fromString(String taskStr, FileBackedTaskManager manager) {
        String[] parts = taskStr.split(DELIMITER);
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        TaskStatus status = TaskStatus.valueOf(parts[3]);
        String description = parts[4];

        switch (type) {
            case TASK:
                Task task = new Task(name, description, status);
                task.setId(id);
                manager.taskStorage.put(id, task);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description, status);
                epic.setId(id);
                manager.epicStorage.put(id, epic);
                return epic;
            case SUBTASK:
                int epicID = Integer.parseInt(parts[5]);
                Subtask subtask = new Subtask(name, description, status, epicID);
                subtask.setId(id);
                manager.subtaskStorage.put(id, subtask);
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
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