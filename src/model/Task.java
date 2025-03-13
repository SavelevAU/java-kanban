package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    //просто задача
    protected String taskName;
    protected String description;
    protected TaskStatus taskStatus;
    protected TaskType taskType;
    protected int Id;
    protected Duration duration = Duration.ofMinutes(0);
    protected LocalDateTime startTime;

    public Task(String taskName, String description, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.description = description;
        this.taskStatus = TaskStatus.NEW;
        this.taskType = TaskType.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        this.taskStatus = TaskStatus.NEW;
        this.taskType = TaskType.TASK;
        this.duration = Duration.ofMinutes(5);
        this.startTime = LocalDateTime.now();
    }

    public Task(String taskName, String description, int id, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.description = description;
        this.taskStatus = TaskStatus.NEW;
        this.taskType = TaskType.TASK;
        this.Id = id;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int id, String taskName, String description, TaskStatus taskStatus, LocalDateTime startTime, Duration duration) {

        this.taskName = taskName;
        this.description = description;
        this.taskStatus = taskStatus;
        this.Id = id;
        this.taskType = TaskType.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }//
    public int getId() {
        return Id;
    }

    Task(String taskName, String description, int id) {
        this.taskName = taskName;
        this.description = description;
        this.Id = id;
        this.duration = Duration.ofMinutes(5);
        this.startTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "SingleTask{" +
                "id=" + Id +
                ", name='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + taskStatus +
                '}';
    }
    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public TaskType getType() {
        return taskType;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Id == task.Id && taskName.equals(task.taskName) && description.equals(task.description) && taskStatus == task.taskStatus;
    }
    @Override
    public int hashCode() {
        return Objects.hash(Id, taskName, description, taskStatus);
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plusMinutes(duration.toMinutes());
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }


}
