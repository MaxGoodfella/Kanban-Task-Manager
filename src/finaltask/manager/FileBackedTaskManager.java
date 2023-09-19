package finaltask.manager;

import finaltask.tasks.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;
    private static CSVManager csvManager = new CSVManager();

    private HistoryManager historyManager;

    public FileBackedTaskManager(File file, HistoryManager historyManager) {
        super(historyManager);
        this.file = file;
        this.historyManager = historyManager;
    }


    private void save() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write(csvManager.getHeading());
            writer.newLine();

            for (Task task : taskStorage.values()) {
                writer.write(csvManager.toString(task));
                writer.newLine();
            }

            for (Epic epic : epicStorage.values()) {
                writer.write(csvManager.toString(epic));
                writer.newLine();
            }

            for (Subtask subtask : subtaskStorage.values()) {
                writer.write(csvManager.toString(subtask));
                writer.newLine();
            }

            writer.newLine();

            writer.write(csvManager.historyToString(historyManager));
            writer.newLine();


        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл", e);
        }


    }

    static FileBackedTaskManager loadFromFile(File file) {

        FileBackedTaskManager manager = new FileBackedTaskManager(file, new InMemoryHistoryManager());


        ArrayList<String> content = FileReader.readFileContents(file.getName());
        String contentString = String.join("\n", content);
        String[] lines = contentString.split("\n");


        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (!line.isBlank()) {

                String[] parts = line.split(",");
                String id = parts[0];
                TaskType type = TaskType.valueOf(parts[1]);
                String name = parts[2];
                TaskStatus status = TaskStatus.valueOf(parts[3]);
                String description = parts[4];


                switch (type) {
                    case TASK:
                        Task task = new Task(name, description, status);
                        task.setId(Integer.valueOf(id));
                        manager.taskStorage.put(Integer.valueOf(id), task);
                        break;
                    case EPIC:
                        Epic epic = new Epic(name, description, status);
                        epic.setId(Integer.valueOf(id));
                        manager.epicStorage.put(Integer.valueOf(id), epic);
                        break;
                    case SUBTASK:
                        int epicID = Integer.parseInt(parts[5]);
                        Subtask subtask = new Subtask(name, description, status, epicID);
                        subtask.setId(Integer.valueOf(id));
                        manager.subtaskStorage.put(Integer.valueOf(id), subtask);
                        break;
                    default:
                        break;
                }

            } else {
                String historyStr = lines[i + 1];
                List<Integer> history = csvManager.historyFromString(historyStr);
                manager.historyManager.setHistory(history);
                break;
            }
        }
        return manager;
    }


    @Override
    public Task createTask(Task task) {
        Task t = super.createTask(task);
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
        save();
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
    public void removeTaskByID(int id) {
        super.removeTaskByID(id);
        save();
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
        save();
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
    public void removeEpicByID(int epicID) {
        super.removeEpicByID(epicID);
        save();
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
        save();
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
    public void removeSubtaskByID(int subtaskID) {
        super.removeSubtaskByID(subtaskID);
        save();
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

}