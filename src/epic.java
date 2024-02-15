import java.util.ArrayList;
import java.util.List;

public class epic extends Task {
    private List<subtask> subtasks;

    public epic(String epicName, String description) {
        super(epicName, description);
        this.subtasks = new ArrayList<>();
    }

    public List<subtask> getSubTasks() {
        return subtasks;
    }

    public void setSubTasks(List<subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" + uin +
                ", name='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + taskStatus +
                ", subTasks=" + subtasks +
                '}';
    }
}

