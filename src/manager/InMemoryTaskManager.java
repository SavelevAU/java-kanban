package manager;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks;

    protected final HashMap<Integer, Epic> epics;

    protected final HashMap<Integer, SubTask> subtasks;

    private final TaskIdGenerator taskIdGenerator;

    private final HistoryManager historyManager;

    protected int currentId;

    protected TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();

        currentId = 0;
        this.taskIdGenerator = new TaskIdGenerator();
        this.historyManager = Managers.getDefaultHistory();
        prioritizedTasks = new TreeSet<>((task1, task2) -> {
            if (task1.getStartTime() == null && task2.getStartTime() == null) {
                return 0; // Оба времени равны null, задачи равны
            }
            if (task1.getStartTime() == null) {
                return -1; // Task1 "меньше", если startTime null
            }
            if (task2.getStartTime() == null) {
                return 1; // Task2 "меньше", если startTime null
            }
            return task1.getStartTime().compareTo(task2.getStartTime());
        });
    }

    @Override
    public void createTask(Task Task) {
        if (Task.getStartTime() == null) {
            Task.setStartTime(LocalDateTime.now());
        }
        if (Task.getDuration() == null) {
            Task.setDuration(Duration.ofMinutes(5));
        }
        if (isTaskIntersection(Task)) {
            return;
        }
        Task.setId(taskIdGenerator.getNewId());
        tasks.put(Task.getId(), Task);
        if (Task.getStartTime() != null) {
            prioritizedTasks.add(Task);
        }
    }

    public void createSubTask(Task Task) {
        if (Task.getStartTime() == null) {
            Task.setStartTime(LocalDateTime.now());
        }
        if (Task.getDuration() == null) {
            Task.setDuration(Duration.ofMinutes(5));
        }
        if (isTaskIntersection(Task)) {
            return;
        }
        Task.setId(taskIdGenerator.getNewId());
        subtasks.put(Task.getId(), (SubTask) Task);
        if (Task.getStartTime() != null) {
            prioritizedTasks.add(Task);
        }
    }

    public void createEpic(Epic Task) {
        if (Task.getStartTime() == null) {
            Task.setStartTime(LocalDateTime.now());
        }
        if (Task.getDuration() == null) {
            Task.setDuration(Duration.ofMinutes(5));
        }
        if (isTaskIntersection(Task)) {
            return;
        }
        Task.setId(taskIdGenerator.getNewId());
        epics.put(Task.getId(), Task);
        if (Task.getStartTime() != null) {
             prioritizedTasks.add(Task);
        }
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
        if (!subtasks.containsKey(taskId)) {
            System.out.println("Такого сабтаска нет");
            return null;
        }
        historyManager.add(subtasks.get(taskId));
        return subtasks.get(taskId);
    }

    @Override
    public void saveSubTask(SubTask subtask) {
        subtask.setId(taskIdGenerator.getNewId());
        subtasks.put(subtask.getId(), subtask);

        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        List<SubTask> list = epic.getSubTasks();

        list.add(subtask);

        epic.setSubTasks(list);
        updateEpic(epic);
    }

    @Override
    public void updateSubTask(SubTask subtask) {
        if (subtask.getStartTime() == null) {
            subtask.setStartTime(LocalDateTime.now());
        }
        if (subtask.getDuration() == null) {
            subtask.setDuration(Duration.ofMinutes(5));
        }
       if (isTaskIntersection(subtask)) {
            return;
        }
        tasks.put(subtask.getId(), subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.remove(subtask);
            prioritizedTasks.add(subtask);
        }
        int epicId = subtask.getEpicId();
        Epic epic = (Epic) tasks.get(epicId);

        calculateEpicStatus(epic);
        epic.changeTerms();
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

        updateEpic(epic);
    }

    @Override
    public void deleteSubTaskById(Integer subTaskId) {
        if (subtasks.containsKey(subTaskId)) {
            SubTask subtask = subtasks.get(subTaskId);
            int epicId = subtask.getEpicId();

            Epic epic = (Epic) epics.get(epicId);

            subtasks.remove(subTaskId);
            if (subtask != null && subtask.getStartTime() != null) {
                prioritizedTasks.remove(subtask);
            }

            List<SubTask> list = epic.getSubTasks();
            list.remove(subtask);

            epic.setSubTasks(list);
            calculateEpicStatus(epic);

        } else {
            System.out.printf("Сабтаски с id = %s нет в базе", subTaskId);
        }

    }

    @Override
    public void deleteTaskById(Integer id) {
            Task task = getTaskById(id);
            if (task != null && task.getStartTime() != null) {
                prioritizedTasks.remove(task);
            }
            tasks.remove(id);
            historyManager.remove(id);

    }
    @Override
    public void deleteEpicById(Integer id) {
        Task task = getEpicById(id);
        if (task != null && task.getStartTime() != null) {
            prioritizedTasks.remove(task);
        }
        epics.remove(id);
        historyManager.remove(id);

    }
    @Override
    public void saveEpic(Task epic) {
        epic.setId(taskIdGenerator.getNewId());
        epics.put(epic.getId(), (Epic) epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        tasks.put(epic.getId(), epic);
    }

    @Override
    public Task getEpicById(Integer epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public List<Task> getAllTask() {
        return tasks.values().stream().toList();
    }

    @Override
    public List<SubTask> getAllSubtaskTask() {
        return subtasks.values().stream().toList();
    }

    @Override
    public List<Epic> getAllEpic() {
        return epics.values().stream().toList();
    }

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
    public void updateTask(Task Task) {
        if (Task.getStartTime() != null && isTaskIntersection(Task)) {
            return;
        }
        tasks.put(Task.getId(), Task);
        if (Task.getStartTime() != null) {
            prioritizedTasks.remove(Task); // Удаляем старую версию задачи из отсортированного списка
            prioritizedTasks.add(Task);   // Добавляем обновленную версию
        }
    }//

    @Override
    public Task getSingleTaskById(Integer taskId) {
        if (!tasks.containsKey(taskId)) {
            return null;
        }
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }
    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public boolean isTaskIntersection(Task task) {
        if (task.getStartTime() != null) {
            for (Task chekTask : getPrioritizedTasks()) {
                if (task.getId() != chekTask.getId() && task.getStartTime().isBefore(chekTask.getEndTime())
                        && task.getEndTime().isAfter(chekTask.getStartTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void clearTasks() {
        for (Task task : tasks.values()) {
            if (task.getStartTime() != null) {
                prioritizedTasks.remove(task);
            }
        }
        for (int id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void clearSubTasks() {
        for (Task task : subtasks.values()) {
            if (task.getStartTime() != null) {
                prioritizedTasks.remove(task);
            }
        }
        for (int id : tasks.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
    }

    @Override
    public void clearEpics() {
        for (Task task : epics.values()) {
            if (task.getStartTime() != null) {
                prioritizedTasks.remove(task);
            }
        }
        for (int id : tasks.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
    }
}