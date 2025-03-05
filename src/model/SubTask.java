package model;
public class SubTask extends Task {
    private int epicId;


    public SubTask(String name, String description, int epicId, Epic epic) {
        super(name, description);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }////

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }////

    public SubTask(String name, String description, int epicId, int Id) {
        super(name, description, Id);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }////

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", id=" + Id +
                ", name='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + taskStatus +
                '}';
    }

    @Override
    public TaskType getType() {
        return taskType;
    }

}
