package model;

import java.util.ArrayList;
import java.util.List;
//
public class Epic extends Task {
    private List<SubTask> subtasks;

    public Epic(String epicName, String description) {
        super(epicName, description);
        this.subtasks = new ArrayList<>();
    }

    public List<SubTask> getSubTasks() {
        return subtasks;
    }

    public void setSubTasks(List<SubTask> subtasks) {
        this.subtasks = subtasks;
    }

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
}

