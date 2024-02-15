public class subtask extends Task {
    private int epicUin;
    public subtask(String name, String description, int epicUin) {
        super(name, description);
        this.epicUin = epicUin;
    }

    public int getEpicUin() {
        return epicUin;
    }

    public void setEpicUin(int epicUin) {
        this.epicUin = epicUin;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicUin +
                ", id=" + uin +
                ", name='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + taskStatus +
                '}';
    }
}
