package manager;

import model.*;

public class CSVFormat {
    static String toString(Task task) {
        String taskString = task.getId() + "," + task.getType() + "," + task.getTaskName() + "," + task.getTaskStatus() + "," + task.getDescription();
        if (task.getType() == TaskType.SUBTASK) {
            taskString = taskString + "," + ((SubTask) task).getEpicId();
        }
        return taskString;
    }

    static Task taskFromString(String value, TaskManager taskManager) throws ManagerLoadException {
        final String[] values = value.split(",");
        if (values.length < 5) {
            throw new ManagerLoadException("Ошибка при чтении строки задачи");
        }
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        if (type == TaskType.TASK) {
            return new Task(id, values[2], values[4], TaskStatus.valueOf(values[3]));
        } else if (type == TaskType.EPIC) {
            return new Epic(values[2], values[4], id);
        } else if (type == TaskType.SUBTASK) {
            if (values.length < 6) {
                throw new ManagerLoadException("Ошибка при чтении строки подзадачи");
            }
            final int epicId = Integer.parseInt(values[5]);
            return new SubTask(values[2], values[4], epicId, id);
        }
        return null;
    }
}
