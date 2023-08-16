package finaltask.tasks;

public class Subtask extends Task {

    private int epicID;


    public Subtask(String name, String description, TaskStatus status, int epicID) {
        super(name, description, status);
        this.epicID = epicID;
    }

    public Subtask(String name, String description) {
        super(name, description);
        this.status = TaskStatus.NEW;
        this.epicID = -1;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }
}