package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface TaskManager {
    void createTask(Task Task);

    void saveSubTask(SubTask subtask);

    void updateSubTask(SubTask subtask);

    void calculateEpicStatus(Epic epic);

    void deleteSubTaskById(Integer subTaskId);

    void saveEpic(Task epic);

    void updateEpic(Epic epic);

    Task getEpicById(Integer epicId);

    Task getTaskById(Integer taskId);

    List<Task> getAllTask();

    List<Task> getAllSubtaskTask();

    List<Task> getAllEpic();

    Task getSubTaskById(Integer taskId);

    @Override
    String toString();

    public void saveTask(Task Task);
    public void updateTask(Task Task);
    public Task getSingleTaskById(Integer taskId);

    public List<Task> getHistory();
    public HistoryManager getHistoryManager();
}//
