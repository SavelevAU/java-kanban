package model;
public class SubTask extends Task {
    private int epicId;
    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

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
}
