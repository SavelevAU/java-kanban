package model;

import java.util.ArrayList;
import java.util.List;
//
public class Epic extends Task {
    private List<SubTask> subtasks;

    public Epic(String epicName, String description) {
        super(epicName, description);
        this.subtasks = new ArrayList<>();
        this.taskType = TaskType.EPIC;
    }

    public Epic(String epicName, String description, int id) {
        super(epicName, description, id);
        this.subtasks = new ArrayList<>();
        this.taskType = TaskType.EPIC;
    }


    public List<SubTask> getSubTasks() {
        return subtasks;
    }

    public void setSubTasks(List<SubTask> subtasks) {
        this.subtasks = subtasks;
    }

    public void addSubtaskToEpic(SubTask subtask) { subtasks.add(subtask); }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" + Id +
                ", name='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + taskStatus +
                ", subTasks=" + subtasks +
                '}';
    }

    @Override
    public TaskType getType() {
        return taskType;
    }
}

