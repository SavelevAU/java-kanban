package manager;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    final private HashMap<Integer, Task> tasks;
    final private TaskIdGenerator taskIdGenerator;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.taskIdGenerator = new TaskIdGenerator();
    }

    public void createTask(Task Task) {
        Task.setId(taskIdGenerator.getNewId());
        tasks.put(Task.getId(), Task);
    }

    public Task getTaskById(Integer taskId) {
        if (!tasks.containsKey(taskId)) {
            return null;
        }
        return tasks.get(taskId);
    }

    public void saveSubTask(SubTask subtask) {
        subtask.setId(taskIdGenerator.getNewId());
        tasks.put(subtask.getId(), subtask);

        int epicId = subtask.getEpicId();
        Epic epic = (Epic) tasks.get(epicId);

        List<SubTask> list = epic.getSubTasks();

        list.add(subtask);

        epic.setSubTasks(list);
        updateepic(epic);
    }

    public void updatesubtask(SubTask subtask) {
        tasks.put(subtask.getId(), subtask);

        int epicId = subtask.getEpicId();
        Epic epic = (Epic) tasks.get(epicId);

        calculateEpicStatus(epic);
    }

    private void calculateEpicStatus(Epic epic) {

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

    public void deleteSubTaskById(Integer subTaskId) {
        if (tasks.containsKey(subTaskId)) {
            SubTask subtask = (SubTask) tasks.get(subTaskId);
            int epicId = subtask.getEpicId();

            Epic epic = (Epic) tasks.get(epicId);

            tasks.remove(subTaskId);

            List<SubTask> list = epic.getSubTasks();
            list.remove(subtask);

            epic.setSubTasks(list);
            calculateEpicStatus(epic);

        } else {
            System.out.printf("Сабтаски с id = %s нет в базе", subTaskId);
        }

    }

    public void saveEpic(Task epic) {
        epic.setId(taskIdGenerator.getNewId());
        tasks.put(epic.getId(), epic);
    }

    public void updateepic(Epic epic) {
        tasks.put(epic.getId(), epic);
    }

    public Task getEpicById(Integer epicId) {
        if (!tasks.containsKey(epicId)) {
            return null;
        }
        return tasks.get(epicId);
    }

    public List<Task> getAllTask() {
        List<Task> allTasks = new ArrayList<>();

        for (Task value : tasks.values()) {
            if (value instanceof Task) {
                allTasks.add(value);
            }
        }
        return allTasks;
    }

    public List<Task> getAllSubtaskTask() {
        List<Task> allTasks = new ArrayList<>();

        for (Task value : tasks.values()) {
            if (value instanceof SubTask) {
                allTasks.add(value);
            }
        }
        return allTasks;
    }

    public List<Task> getAllEpic() {
        List<Task> allTasks = new ArrayList<>();

        for (Task value : tasks.values()) {
            if (value instanceof Epic) {
                allTasks.add(value);
            }
        }
        return allTasks;
    }

    @Override
    public String toString() {
        return "taskManager.TaskManager{" +
                "tasks=" + tasks +
                '}';
    }
}
