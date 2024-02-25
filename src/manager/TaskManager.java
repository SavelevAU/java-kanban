package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void createTask(Task Task);

    Task getTaskById(Integer taskId);

    void saveSubTask(SubTask subtask);

    void updatesubtask(SubTask subtask);

    default void calculateEpicStatus(Epic epic) {

        List<TaskStatus> statuses = new ArrayList<>();

        for (SubTask task : epic.getSubTasks()) {
            statuses.add(task.getTaskStatus());
        }

        if (statuses.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
        }


        if (statuses.contains(TaskStatus.NEW) &&
                !statuses.contains(TaskStatus.IN_PROGRESS) && !statuses.contains(TaskStatus.DONE)) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (statuses.contains(TaskStatus.DONE) &&
                !statuses.contains(TaskStatus.NEW) && !statuses.contains(TaskStatus.IN_PROGRESS)) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }

        updateepic(epic);
    }

    void deleteSubTaskById(Integer subTaskId);

    void saveEpic(Task epic);

    void updateepic(Epic epic);

    Task getEpicById(Integer epicId);

    ArrayList<Task> getTaskById(List<Integer> taskIds);

    List<Task> getAllTask();

    List<Task> getAllSubtaskTask();

    List<Task> getAllEpic();

    Task getSubTaskById(Integer taskId);

    @Override
    String toString();

    public List<Task> getHistory();

    public void saveTask(Task Task);
    public void updateTask(Task Task);
}
