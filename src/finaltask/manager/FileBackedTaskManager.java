package finaltask.manager;

import finaltask.manager.exceptions.ManagerSaveException;
import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File file;
    public static CSVManager csvManager = new CSVManager();

    public FileBackedTaskManager(File file, HistoryManager historyManager) {
        super(historyManager);
        this.file = file;
    }


    public void save() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write(csvManager.getHeading());
            writer.newLine();

            for (Task task : taskStorage.values()) {
                writer.write(csvManager.taskToString(task));
                writer.newLine();
            }

            for (Epic epic : epicStorage.values()) {
                writer.write(csvManager.taskToString(epic));
                writer.newLine();
            }

            for (Subtask subtask : subtaskStorage.values()) {
                writer.write(csvManager.taskToString(subtask));
                writer.newLine();
            }

            writer.newLine();

            writer.write(csvManager.historyToString(historyManager));


        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно записать файл", e);
        }

    }

    public static FileBackedTaskManager loadFromFile(File file, HistoryManager historyManager) {

        FileBackedTaskManager manager = new FileBackedTaskManager(file, new InMemoryHistoryManager());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    csvManager.taskFromString(line, manager);
                } else {
                    String historyStr = reader.readLine();
                    List<Integer> historyIDs = csvManager.historyFromString(historyStr);
                    for (Integer id : historyIDs) {
                        if (id == 0) {
                            break;
                        }

                        if (manager.taskStorage.containsKey(id)) {
                            manager.getTaskByID(id);
                        } else if (manager.subtaskStorage.containsKey(id)) {
                            manager.getSubtaskByID(id);
                        } else if (manager.epicStorage.containsKey(id)) {
                            manager.getEpicByID(id);
                        }
                    }
                    manager.historyManager.setHistory(historyIDs);
                    break;
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл", e);
        }
        return manager;
    }



    @Override
    public Task createTask(Task task) {
        Task t = super.createTask(task);
        System.out.println("Задача создана: " + task); // отладка
        save();
        return t;
    }

    @Override
    public Task getTaskByID(int id) {
        Task t = super.getTaskByID(id);
        save();
        return t;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> alt = super.getAllTasks();
        // save();
        return alt;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public int removeTaskByID(int id) {
        super.removeTaskByID(id);
        save();
        return id;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic e = super.createEpic(epic);
        save();
        return e;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic e = super.getEpicByID(id);
        save();
        return e;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> ale = super.getAllEpics();
        // save();
        return ale;
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public int removeEpicByID(int epicID) {
        super.removeEpicByID(epicID);
        save();
        return epicID;
    }

    @Override
    public void updateEpicStatus(int epicID) {
        super.updateEpicStatus(epicID);
        save();
    }

    @Override
    public void actualizeEpic(int epicID, InMemoryTaskManager inMemoryTaskManager) {
        super.actualizeEpic(epicID, inMemoryTaskManager);
        save();
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask s = super.createSubtask(subtask);
        save();
        return s;
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        Subtask s = super.getSubtaskByID(id);
        save();
        return s;
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> als = super.getAllSubtasks();
        // save();
        return als;
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public int removeSubtaskByID(int subtaskID) {
        super.removeSubtaskByID(subtaskID);
        save();
        return subtaskID;
    }

    @Override
    public void updateAllEpicsStatus() {
        super.updateAllEpicsStatus();
        save();
    }

    @Override
    public List<Task> getHistory() {
        List<Task> lt = super.getHistory();
        save();
        return lt;
    }

    @Override
    public void calculateEpicStartTime(int epicId) {
        super.calculateEpicStartTime(epicId);
        save();
    }

    @Override
    public void calculateEpicDuration(int epicId) {
        super.calculateEpicDuration(epicId);
        save();
    }

    @Override
    public void calculateEpicEndTime(int epicId) {
        super.calculateEpicEndTime(epicId);
        save();
    }

    @Override
    public List<Subtask> getEpicSubtasksByEpicID(int epicID) {
        List<Subtask> esbeid = super.getEpicSubtasksByEpicID(epicID);
        save();
        return esbeid;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        List<Task> pt = super.getPrioritizedTasks();
        save();
        return pt;
    }

    @Override
    public boolean isIntersection(Task task) {
        return super.isIntersection(task);
    }

}