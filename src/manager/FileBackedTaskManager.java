package manager;

import model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

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
    public void calculateEpicStatus(Epic epic) {
        super.calculateEpicStatus(epic);
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
    public void saveTask(Task Task) {
        super.saveTask(Task);
        save();
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
            if (blocks.length == 2) {
                String[] valuesOfTasks = blocks[0].split("\n");
                String[] valuesOfHistory = blocks[1].split(",");
                loadTasks(taskManager, valuesOfTasks);
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
                Task task = taskFromString(taskString, taskManager);
                if (task != null) {
                    taskManager.addTaskToMap(task);
                    taskManager.currentId = Integer.max(task.getId(), taskManager.currentId);
                }
            }
            isHeader = false;
        }
    }

    private void addTaskToMap(Task task) {
        TaskType type = task.getType();
        if (type == TaskType.TASK) {
            tasks.put(task.getId(), task);
        } else if (type == TaskType.EPIC) {
            epics.put(task.getId(), (Epic) task);
        } else if (type == TaskType.SUBTASK) {
            subtasks.put(task.getId(), (SubTask) task);
            Epic epic = ((SubTask) task).getEpic();
            epic.addSubtaskToEpic((SubTask) task);
        }
    }
    private static void loadHistory(FileBackedTaskManager taskManager, String[] valuesOfHistory) {
        //очищаем историю
        for (Task task : taskManager.getHistory()) {
            taskManager.getHistoryManager().remove(task.getId());
        }
        for (String value : valuesOfHistory) {
            final int id = Integer.parseInt(value);
            if (taskManager.tasks.containsKey(id)) {
                taskManager.getHistoryManager().add(taskManager.getTaskById(id));
            } else if (taskManager.epics.containsKey(id)) {
                taskManager.getHistoryManager().add(taskManager.getEpicById(id));
            } else if (taskManager.subtasks.containsKey(id)) {
                taskManager.getHistoryManager().add(taskManager.getSubTaskById(id));
            }
        }
    }
    public void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTask()) {
                writer.write(toString(task, "TASK") + "\n");
            }
            for (Task epic : getAllEpic()) {
                writer.write(toString(epic, "EPIC") + "\n");
            }
            for (Task subtask : getAllSubtaskTask()) {
                writer.write(toString(subtask, "SUBTASK") + "\n");
            }
            writer.write("\n");
            for (Task task : getHistory()) {
                writer.write(task.getId() + ",");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных", e);
        }
    }

    private static String toString(Task task, String type) {
        String taskString = task.getId() + "," + task.getType() + "," + task.getTaskName() + "," + task.getTaskStatus() + "," + task.getDescription();
        if (task.getType() == TaskType.SUBTASK) {
            taskString = taskString + "," + ((SubTask) task).getEpicId();
        }
        return taskString;
    }

    private static Task taskFromString(String value, TaskManager taskManager) throws ManagerLoadException {
        final String[] values = value.split(",");
        if (values.length < 5) {
            throw new ManagerLoadException("Ошибка при чтении строки задачи");
        }
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        if (type == TaskType.TASK) {
            return new Task(id, values[2], values[4], TaskStatus.valueOf(values[3]));
        } else if (type == TaskType.EPIC) {
            return new Epic(values[2], values[4]);
        } else if (type == TaskType.SUBTASK) {
            if (values.length < 6) {
                throw new ManagerLoadException("Ошибка при чтении строки подзадачи");
            }
            final int epicId = Integer.parseInt(values[5]);
            return new SubTask(values[2], values[4], epicId);
        }
        return null;
    }
}
