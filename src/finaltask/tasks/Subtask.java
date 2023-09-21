package finaltask.tasks;


public class Subtask extends Task {

    private int epicID;


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


    public Boolean equals(Subtask subtaskToCompare) {
        if (!this.getId().equals(subtaskToCompare.getId())) {
            return false;
        }

        if (!this.getName().equals(subtaskToCompare.getName())) {
            return false;
        }

        if (!this.getDescription().equals(subtaskToCompare.getDescription())) {
            return false;
        }

        if (!this.getType().equals(subtaskToCompare.getType())) {
            return false;
        }

        if (!this.getStatus().equals(subtaskToCompare.getStatus())) {
            return false;
        }

        return true;
    }

}