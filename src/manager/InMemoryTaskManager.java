package manager;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final  HashMap<Integer, Task> tasks;

    private final  TaskIdGenerator taskIdGenerator;

    private final  HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.taskIdGenerator = new TaskIdGenerator();
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void createTask(Task Task) {
        Task.setId(taskIdGenerator.getNewId());
        tasks.put(Task.getId(), Task);
    }

    @Override
    public Task getTaskById(Integer taskId) {
        if (!tasks.containsKey(taskId)) {
            return null;
        }
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }
    @Override
    public Task getSubTaskById(Integer taskId) {
        if (!tasks.containsKey(taskId)) {
            System.out.println("Такого сабтаска нет");
            return null;
        }
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }
    @Override
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

    @Override
    public void updatesubtask(SubTask subtask) {
        tasks.put(subtask.getId(), subtask);

        int epicId = subtask.getEpicId();
        Epic epic = (Epic) tasks.get(epicId);

        calculateEpicStatus(epic);
    }

    @Override
    public void calculateEpicStatus(Epic epic) {
        TaskManager.super.calculateEpicStatus(epic);
    }

    @Override
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

    @Override
    public void saveEpic(Task epic) {
        epic.setId(taskIdGenerator.getNewId());
        tasks.put(epic.getId(), epic);
    }

    @Override
    public void updateepic(Epic epic) {
        tasks.put(epic.getId(), epic);
    }

    @Override
    public Task getEpicById(Integer epicId) {
        if (!tasks.containsKey(epicId)) {
            return null;
        }
        historyManager.add(tasks.get(epicId));
        return tasks.get(epicId);
    }

    @Override
    public List<Task> getAllTask() {
        List<Task> allTasks = new ArrayList<>();

        for (Task value : tasks.values()) {
            if (value instanceof Task) {
                allTasks.add(value);
            }
        }
        return allTasks;
    }

    @Override
    public List<Task> getAllSubtaskTask() {
        List<Task> allTasks = new ArrayList<>();

        for (Task value : tasks.values()) {
            if (value instanceof SubTask) {
                allTasks.add(value);
            }
        }
        return allTasks;
    }

    @Override
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
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
    @Override
    public void saveTask(Task Task) {
        Task.setId(taskIdGenerator.getNewId());
        tasks.put(Task.getId(), Task);
    }
    @Override
    public void updateTask(Task Task) {

        tasks.put(Task.getId(), Task);
    }//
    @Override
    public Task getSingleTaskById(Integer taskId) {
        if (!tasks.containsKey(taskId)) {
            return null;
        }
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }
}
