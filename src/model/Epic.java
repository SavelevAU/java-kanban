package model;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;
//
public class Epic extends Task {
    private List<SubTask> subtasks;
    private LocalDateTime endTime = LocalDateTime.of(1,1,1,1,1);

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
//NEW
    public void changeTerms() {
        setStartTime(null);
        endTime = null;
        setDuration(Duration.ofMinutes(0));
        for (SubTask subtask : subtasks) {
            if (startTime == null) {
                setStartTime(subtask.getStartTime());
            } else if (startTime.isAfter(subtask.getStartTime())) {
                setStartTime(subtask.getStartTime());
            }
            if (endTime == null) {
                endTime = subtask.getEndTime();
            } else if (endTime.isBefore(subtask.getEndTime())) {
                endTime = subtask.getEndTime();
            }
        }
        if (startTime != null & endTime != null) {
            setDuration(Duration.between(startTime, endTime));
        }
    }

    @Override
    public LocalDateTime getEndTime() {
            if (endTime == LocalDateTime.of(1,1,1,1,1) || endTime == null) {
                return startTime.plusMinutes(duration.toMinutes());
            }
        return endTime;
    }
}

