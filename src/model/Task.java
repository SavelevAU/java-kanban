package model;

import java.util.Objects;

public class Task {
    //просто задача
    protected String taskName;
    protected String description;
    protected TaskStatus taskStatus;
    protected int Id;

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        this.taskStatus = TaskStatus.NEW;
    }
    public Task(int id, String taskName, String description, TaskStatus taskStatus) {

        this.taskName = taskName;
        this.description = description;
        this.taskStatus = taskStatus;
        this.Id = id;
    }//
    public int getId() {
        return Id;
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
}
