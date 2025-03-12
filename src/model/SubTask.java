package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;


    public SubTask(String name, String description, int epicId, Epic epic, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }////

    public SubTask(String name, String description, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }////

    public SubTask(String name, String description, int epicId, int Id, LocalDateTime startTime, Duration duration) {
        super(name, description, Id, startTime, duration);
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
