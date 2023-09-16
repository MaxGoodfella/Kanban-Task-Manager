package finaltask.tasks;

public class Subtask extends Task {

    private int epicID;
    // protected TaskType type; // возможно не нужно


    public Subtask(String name, String description, TaskStatus status, int epicID) {
        super(name, description, status);
        this.epicID = epicID;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(String name, String description) {
        super(name, description);
        this.status = TaskStatus.NEW;
        this.epicID = -1;
        this.type = TaskType.SUBTASK;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }
}