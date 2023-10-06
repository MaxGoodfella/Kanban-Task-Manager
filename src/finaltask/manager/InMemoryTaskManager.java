package finaltask.manager;

import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;
import finaltask.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{

    private int generatedID = 0;

    private int generateID() {
        return ++generatedID;
    }

    protected HashMap<Integer, Task> taskStorage = new HashMap<>();
    protected HashMap<Integer, Epic> epicStorage = new HashMap<>();
    protected HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();

    protected final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public Task createTask(Task task) {
        int id = generateID();
        task.setId(id);
        taskStorage.put(id, task);
        return task;
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = taskStorage.get(id);
        historyManager.addTask(task);
        return task;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    @Override
    public void removeAllTasks() {
        taskStorage.clear();
    }

    @Override
    public void updateTask(Task task) {
        Task saved = taskStorage.get(task.getId());

        if (saved == null) {
            return;
        }

        taskStorage.put(task.getId(), task);
    }

    @Override
    public void removeTaskByID(int id) {
        taskStorage.remove(id);
        historyManager.remove(id);
    }

    @Override
    public Epic createEpic(Epic epic) {

        if (epic == null) {
            return null;
        }

        int id = generateID();
        epic.setId(id);
        epicStorage.put(id, epic);
        return epic;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = epicStorage.get(id);
        historyManager.addTask(epic);
        return epic;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicStorage.values());
    }

    @Override
    public void removeAllEpics() {
        subtaskStorage.clear();
        epicStorage.clear();
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epicStorage.get(epic.getId());

        if (saved == null) {
            return;
        }

        epicStorage.put(epic.getId(), epic);
    }

    @Override
    public int removeEpicByID(int epicID) {
        Epic epic = epicStorage.remove(epicID);
        for (Integer subtaskID : epic.getAllSubtaskIDs()) {
            subtaskStorage.remove(subtaskID);
            historyManager.remove(subtaskID);
        }
        historyManager.remove(epicID);

        return epicID;
    }

    @Override
    public void updateEpicStatus(int epicID) {
        Epic epic = epicStorage.get(epicID);
        if (epic != null) {
            boolean allSubtasksDone = true;
            boolean anySubtaskInProgress = false;

            for (Integer subtaskID : epic.getAllSubtaskIDs()) {
                Subtask subtask = subtaskStorage.get(subtaskID);
                if (subtask != null) {
                    TaskStatus subtaskStatus = subtask.getStatus();
                    if (subtaskStatus != TaskStatus.DONE) {
                        allSubtasksDone = false;
                        if (subtaskStatus == TaskStatus.IN_PROGRESS) {
                            anySubtaskInProgress = true;
                        }
                    }
                }
            }

            if (allSubtasksDone) {
                epic.setStatus(TaskStatus.DONE);
            } else if (anySubtaskInProgress) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            } else {
                epic.setStatus(TaskStatus.NEW);
            }
        }
    }

    @Override
    public void actualizeEpic(int epicID, InMemoryTaskManager inMemoryTaskManager) {

        Epic epic = epicStorage.get(epicID);

        if (epic != null) {
            updateEpicStatus(epicID);
            epicStorage.put(epicID, epic);
        }
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        int id = generateID();
        subtask.setId(id);
        subtaskStorage.put(id, subtask);

        Epic epic = epicStorage.get(subtask.getEpicID());
        if (epic != null) {
            epic.addSubtaskID(id);
            updateEpicStatus(epic.getId());
        }
        return subtask;
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        Subtask subtask = subtaskStorage.get(id);
        historyManager.addTask(subtask);
        return subtask;
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskStorage.values());
    }

    @Override
    public void removeAllSubtasks() {
        subtaskStorage.clear();
        updateAllEpicsStatus();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask saved = subtaskStorage.get(subtask.getId());

        if (saved == null) {
            return;
        }

        subtaskStorage.put(subtask.getId(), subtask);

        int epicID = subtask.getEpicID();
        updateEpicStatus(epicID);
        updateAllEpicsStatus();
    }

    @Override
    public void removeSubtaskByID(int subtaskID) {
        Subtask subtask = subtaskStorage.get(subtaskID);

        if (subtask != null) {
            int epicID = subtask.getEpicID();

            Epic epic = epicStorage.get(epicID);
            if (epic != null) {
                epic.getAllSubtaskIDs().remove(Integer.valueOf(subtaskID));
                if (epic.getAllSubtaskIDs().isEmpty()) {
                    epic.setStatus(TaskStatus.NEW);
                } else {
                    updateEpicStatus(epicID);
                }
            }
            subtaskStorage.remove(subtaskID);
            historyManager.remove(subtaskID);
        }
        updateAllEpicsStatus();
    }

    @Override
    public void updateAllEpicsStatus() { //
        for (Epic epic : epicStorage.values()) {
            updateEpicStatus(epic.getId());
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}