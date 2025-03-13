package manager;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVFormat {
    public static String toString(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String startTime = "";
        if (task.getStartTime() != null) {
            startTime = task.getStartTime().format(formatter);
        }
        String taskString = task.getId() + "," + task.getType() + "," + task.getTaskName() + "," + task.getTaskStatus()
                            + "," + task.getDescription() + "," + startTime + "," + task.getDuration().toMinutes();
        if (task.getType() == TaskType.SUBTASK) {
            taskString = taskString + "," + ((SubTask) task).getEpicId();
        }
        return taskString;
    }

    public static Task taskFromString(String value, TaskManager taskManager) throws ManagerLoadException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        final String[] values = value.split(",");
        if (values.length < 7) {
            throw new ManagerLoadException("Ошибка при чтении строки задачи");
        }
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        if (type == TaskType.TASK) {
            return new Task(id, values[2], values[4], TaskStatus.valueOf(values[3]), LocalDateTime.parse(values[5], formatter), Duration.ofMinutes(Integer.parseInt(values[6])));
        } else if (type == TaskType.EPIC) {
            return new Epic(values[2], values[4], id);
        } else if (type == TaskType.SUBTASK) {
            if (values.length < 8) {
                throw new ManagerLoadException("Ошибка при чтении строки подзадачи");
            }
            final int epicId = Integer.parseInt(values[7]);
            LocalDateTime startTime = null;
            if (! values[5].isEmpty()) {
                startTime = LocalDateTime.parse(values[5], formatter);
            }
            return new SubTask(values[2], values[4], epicId, id, startTime, Duration.ofMinutes(Integer.parseInt(values[6])));
        }
        return null;
    }
}
