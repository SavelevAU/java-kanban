package manager;

import model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.TreeSet;


public class FileBackedTaskManager extends  InMemoryTaskManager {
    private final File file;


    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void createTask(Task Task) {
        super.createTask(Task);
        save();
    }

    public void createSubTask(Task Task) {
        super.createSubTask(Task);
        save();
    }

    public void createEpic(Task Task) {
        super.createEpic(Task);
        save();
    }

    @Override
    public void saveSubTask(SubTask subtask) {
        super.saveSubTask(subtask);
        save();
    }

    @Override
    public void updateSubTask(SubTask subtask) {
        super.updateSubTask(subtask);
        save();
    }

    @Override
    public void deleteSubTaskById(Integer subTaskId) {
        super.deleteSubTaskById(subTaskId);
        save();
    }

    @Override
    public void saveEpic(Task epic) {
        super.saveEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Task getEpicById(Integer epicId) {
        final Task task = super.getEpicById(epicId);
        save();
        return task;
    }

    @Override
    public Task getTaskById(Integer taskId) {
        final Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Task getSubTaskById(Integer taskId) {
        final Task task = super.getSubTaskById(taskId);
        save();
        return task;
    }

    @Override
    public void updateTask(Task Task) {
        super.updateTask(Task);
        save();
    }

    @Override
    public Task getSingleTaskById(Integer taskId) {
        final Task task = super.getSingleTaskById(taskId);
        save();
        return task;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerLoadException {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            String contents = Files.readString(file.toPath());
            String[] blocks = contents.split("\n\n");

            String[] valuesOfTasks = blocks[0].split("\n");
            loadTasks(taskManager, valuesOfTasks);
            if (blocks.length == 2) {
                String[] valuesOfHistory = blocks[1].split(",");
                loadHistory(taskManager, valuesOfHistory);
            }
        } catch (IOException ex) {
            throw new ManagerLoadException("Ошибка чтения из файла", ex);
        }

        return taskManager;
    }

    private static void loadTasks(FileBackedTaskManager taskManager, String[] valuesOfTasks) throws ManagerLoadException {
        boolean isHeader = true;
        for (String taskString : valuesOfTasks) {
            if (!isHeader) {
                Task task = CSVFormat.taskFromString(taskString, taskManager);
                if (task != null) {
                    taskManager.addTaskToMap(task, taskManager);
                    taskManager.currentId = Integer.max(task.getId(), taskManager.currentId);
                }
            }
            isHeader = false;
        }
    }

    private void addTaskToMap(Task task, FileBackedTaskManager taskManager) {
        TaskType type = task.getType();

        if (type == TaskType.TASK) {
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        } else if (type == TaskType.EPIC) {

            tasks.put(task.getId(), (Epic) task);

        } else if (type == TaskType.SUBTASK) {

            final int id = ((SubTask) task).getEpicId();
            tasks.put(task.getId(), (SubTask) task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
            Epic epic = (Epic) taskManager.getEpicById(id);
            epic.addSubtaskToEpic((SubTask) task);
        }
    }

    private static void loadHistory(FileBackedTaskManager taskManager, String[] valuesOfHistory) {
        //очищаем историю
        HistoryManager historyManager = Managers.getDefaultHistory();
        for (Task task : taskManager.getHistory()) {
            historyManager.getHistory().remove(task.getId());


        }
        for (String value : valuesOfHistory) {
            final int id = Integer.parseInt(value);
            if (taskManager.tasks.containsKey(id)) {
                historyManager.getHistory().add(taskManager.getTaskById(id));
            } else if (taskManager.epics.containsKey(id)) {
                historyManager.getHistory().add(taskManager.getEpicById(id));
            } else if (taskManager.subtasks.containsKey(id)) {
                historyManager.getHistory().add(taskManager.getSubTaskById(id));
            }
        }
    }
    private void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTask()) {

                writer.write(CSVFormat.toString(task) + "\n");
            }

            writer.write("\n");
            for (Task task : getHistory()) {
                writer.write(task.getId() + ",");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных", e);
        }
    }


    @Override
    public boolean isTaskIntersection(Task task) {
        return super.isTaskIntersection(task);
    }
}
