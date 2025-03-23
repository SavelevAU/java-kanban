package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    void createTask(Task Task);
    void createSubTask(Task Task);
    void createEpic(Epic Task);

    void saveSubTask(SubTask subtask);

    void updateSubTask(SubTask subtask);


    void deleteSubTaskById(Integer subTaskId);

    void deleteTaskById(Integer subTaskId);

    void deleteEpicById(Integer epicId);

    void clearTasks();
    void clearSubTasks();
    void clearEpics();

    void saveEpic(Task epic);

    void updateEpic(Epic epic);

    Task getEpicById(Integer epicId);

    Task getTaskById(Integer taskId);

    List<Task> getAllTask();

    List<SubTask> getAllSubtaskTask();

    List<Epic> getAllEpic();

    Task getSubTaskById(Integer taskId);

    void updateTask(Task Task);
    Task getSingleTaskById(Integer taskId);

    public List<Task> getHistory();

    Set<Task> getPrioritizedTasks();

    boolean isTaskIntersection(Task task);
}//
