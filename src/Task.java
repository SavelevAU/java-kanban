public class Task {
    //просто задача
    protected String taskName;
    protected String description;
    protected enum_status taskStatus;
    protected int uin;

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        this.taskStatus = enum_status.NEW;
    }
    public int getUin() {
        return uin;
    }

    @Override
    public String toString() {
        return "SingleTask{" +
                "id=" + uin +
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

    public enum_status getTaskStatus() {
        return taskStatus;
    }

    public void setUin(int uin) {
        this.uin = uin;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskStatus(enum_status taskStatus) {
        this.taskStatus = taskStatus;
    }
}
