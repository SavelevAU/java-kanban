import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private TaskUinGenerator taskUinGenerator;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.taskUinGenerator = new TaskUinGenerator();
    }

    public void saveTask(Task Task) {
        Task.setUin(taskUinGenerator.getNewUin());
        tasks.put(Task.getUin(), Task);
    }

    public Task getTaskByUin(Integer taskUin) {
        if (!tasks.containsKey(taskUin)) {
            return null;
        }
        return tasks.get(taskUin);
    }

    public void saveSubTask(subtask subtask) {
        subtask.setUin(taskUinGenerator.getNewUin());
        tasks.put(subtask.getUin(), subtask);

        int epicUin = subtask.getEpicUin();
        epic epic = (epic) tasks.get(epicUin);

        List<subtask> list = epic.getSubTasks();

        list.add(subtask);

        epic.setSubTasks(list);
        updateepic(epic);
    }

    public void updatesubtask(subtask subtask) {
        tasks.put(subtask.getUin(), subtask);

        int epicUin = subtask.getEpicUin();
        epic epic = (epic) tasks.get(epicUin);

        calculateEpicStatus(epic);
    }

    private void calculateEpicStatus(epic epic) {

        List<enum_status> statuses = new ArrayList<>();

        for (subtask task : epic.getSubTasks()) {
            statuses.add(task.getTaskStatus());
        }

        if (statuses.isEmpty()) {
            epic.setTaskStatus(enum_status.NEW);
        }


        if (statuses.contains(enum_status.NEW) &&
                !statuses.contains(enum_status.IN_PROGRESS) && !statuses.contains(enum_status.DONE)) {
            epic.setTaskStatus(enum_status.NEW);
        } else if (statuses.contains(enum_status.DONE) &&
                !statuses.contains(enum_status.NEW) && !statuses.contains(enum_status.IN_PROGRESS)) {
            epic.setTaskStatus(enum_status.DONE);
        } else {
            epic.setTaskStatus(enum_status.IN_PROGRESS);
        }

        updateepic(epic);
    }

    public void deleteSubTaskByUin(Integer subTaskUin) {
        if (tasks.containsKey(subTaskUin)) {
            subtask subtask = (subtask) tasks.get(subTaskUin);
            int epicUin = subtask.getEpicUin();

            epic epic = (epic) tasks.get(epicUin);

            tasks.remove(subTaskUin);

            List<subtask> list = epic.getSubTasks();
            list.remove(subtask);

            epic.setSubTasks(list);
            calculateEpicStatus(epic);

        } else {
            System.out.printf("Сабтаски с id = %s нет в базе", subTaskUin);
        }

    }

    public void saveEpic(Task epic) {
        epic.setUin(taskUinGenerator.getNewUin());
        tasks.put(epic.getUin(), epic);
    }

    public void updateepic(epic epic) {
        tasks.put(epic.getUin(), epic);
    }

    public Task getEpicByUin(Integer epicUin) {
        if (!tasks.containsKey(epicUin)) {
            return null;
        }
        return tasks.get(epicUin);
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
            if (value instanceof subtask) {
                allTasks.add(value);
            }
        }
        return allTasks;
    }

    public List<Task> getAllEpic() {
        List<Task> allTasks = new ArrayList<>();

        for (Task value : tasks.values()) {
            if (value instanceof epic) {
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
