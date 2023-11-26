package finaltask.manager;

import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;
import finaltask.tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager{

    protected int generatedID = 0;

    private int generateID() {
        return ++generatedID;
    }


    protected HashMap<Integer, Task> taskStorage = new HashMap<>();
    protected HashMap<Integer, Epic> epicStorage = new HashMap<>();
    protected HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();

    protected final HistoryManager historyManager;

    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.nullsLast((o1, o2) -> {
        if (o1.getStartTime() != null && o2.getStartTime() != null) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        } else if (o1 == o2) {
            return 0;
        } else {
            return 1;
        }
    }));

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
    public int removeTaskByID(int id) {
        taskStorage.remove(id);
        historyManager.remove(id);
        return id;
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

        Epic epic = epicStorage.get(epicID);

        if (epic != null) {
            for (Integer subtaskID : epic.getAllSubtaskIDs()) {
                subtaskStorage.remove(subtaskID);
                historyManager.remove(subtaskID);
            }
            epicStorage.remove(epicID);
            historyManager.remove(epicID);
            return epicID;
        } else {
            return -1;
        }
    }

    @Override
    public void updateEpicStatus(int epicID) {
        Epic epic = epicStorage.get(epicID);
        if (epic != null) {
            boolean allSubtasksDone = true;
            boolean anySubtaskInProgress = false;

            for (Integer subtaskID : epic.getAllSubtaskIDs()) {
                if (subtaskID != 0) {
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
    public void calculateEpicStartTime(int epicId) {
        List<LocalDateTime> test = new ArrayList<>();
        if (epicStorage.values().isEmpty()) {
            return;
        }
        for (Epic epic : epicStorage.values()) {
            if (Objects.equals(epic.getId(), epicId)) {
                List<Subtask> epicSubtasks = getEpicSubtasksByEpicID(epic.getId());
                if (epicSubtasks.isEmpty()) {
                    return;
                }
                for (Subtask subtask : epicSubtasks) {
                    if (subtask.getStartTime() != null) {
                        test.add(subtask.getStartTime());
                        LocalDateTime min = Collections.min(test);
                        epic.setStartTime(min);
                        calculateEpicDuration(epic.getId());
                    }
                }
            }
        }
    }

    @Override
    public void calculateEpicDuration(int epicId) {
        Duration duration = Duration.ZERO;
        if (epicStorage.values().isEmpty()) {
            return;
        }
        for (Epic epic : epicStorage.values()) {
            if (Objects.equals(epic.getId(), epicId)) {
                List<Subtask> epicSubtasks = getEpicSubtasksByEpicID(epic.getId());
                if (epicSubtasks.isEmpty()) {
                    return;
                }
                for (Subtask subtask : epicSubtasks) {
                    if (subtask.getDuration() == null) {
                        epic.setDuration(duration);
                        return;
                    }
                    duration = duration.plus(subtask.getDuration());
                    epic.setDuration(duration);
                }
            }
        }
    }

    @Override
    public void calculateEpicEndTime(int epicId) {
        List<LocalDateTime> test = new ArrayList<>();
        if (epicStorage.values().isEmpty()) {
            return;
        }
        for (Epic epic : epicStorage.values()) {
            if (Objects.equals(epic.getId(), epicId)) {
                List<Subtask> epicSubtasks = getEpicSubtasksByEpicID(epic.getId());
                if (epicSubtasks.isEmpty()) {
                    if (epic.getStartTime() != null && epic.getDuration() != null) {
                        LocalDateTime endTime = epic.getStartTime().plus(epic.getDuration());
                        epic.setEndTime(endTime);
                    }
                    return;
                }
                for (Subtask subtask : epicSubtasks) {
                    if (subtask.getEndTime() != null) {
                        test.add(subtask.getEndTime());
                        LocalDateTime max = Collections.max(test);
                        epic.setEndTime(max);
                    }
                }
            }
        }
    }


    @Override
    public Subtask createSubtask(Subtask subtask) {
//        int id = generateID();
//        subtask.setId(id);
//        subtaskStorage.put(id, subtask);
//
//        Epic epic = epicStorage.get(subtask.getEpicID());
//        if (epic != null) {
//            epic.addSubtaskID(id);
//            updateEpicStatus(epic.getId());
//        }
//
//        return subtask;

        int id = generateID();
        subtask.setId(id);
        subtaskStorage.put(id, subtask);

        Integer epicID = subtask.getEpicID();
        if (epicID != null) {
            Epic epic = epicStorage.get(epicID);
            if (epic != null) {
                epic.addSubtaskID(id);
                updateEpicStatus(epicID);
            } else {
                System.err.println("Epic not found for subtask ID: " + epicID);
            }
        } else {
            System.err.println("EpicID is null for subtask: " + subtask);
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
    public List<Subtask> getEpicSubtasksByEpicID(int epicID) {
        List<Subtask> epicsSubtasks = new ArrayList<>();
        for (Subtask subtask : subtaskStorage.values()) {
            if (Objects.equals(subtask.getEpicID(), epicID)) {
                epicsSubtasks.add(subtask);
            }
        }
        return epicsSubtasks;
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
    public int removeSubtaskByID(int subtaskID) {
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
        return subtaskID;
    }

    @Override
    public void updateAllEpicsStatus() {
        for (Epic epic : epicStorage.values()) {
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        prioritizedTasks.addAll(taskStorage.values());
        prioritizedTasks.addAll(epicStorage.values());
        prioritizedTasks.addAll(subtaskStorage.values());

        return new ArrayList<>(prioritizedTasks);
    }


    @Override
    public boolean isIntersection(Task task) {
        if (task == null || task.getStartTime() == null || prioritizedTasks.isEmpty()) {
            return false;
        }

        for (Task tasksPrioritized : getPrioritizedTasks()) {
            if (Objects.equals(tasksPrioritized.getId(), task.getId())) {
                continue;
            }
            if (tasksPrioritized.getStartTime() == null) {
                continue;
            }
            if (task.getEndTime().isBefore(tasksPrioritized.getStartTime()) || task.getStartTime().isAfter(tasksPrioritized.getEndTime())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}