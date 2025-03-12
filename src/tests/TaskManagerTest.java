package tests;
import manager.*;
import org.junit.jupiter.api.BeforeEach;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest<T extends TaskManager> {

    Managers managers;
    HistoryManager historyManager;
    private TaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {

        inMemoryTaskManager = managers.getDefault();
        historyManager = Managers.getDefaultHistory();;

    }

    @Test
    void shouldCalculateEpicStatus() {
        // Создаем Epic и связанные подзадачи
        Epic epic = new Epic("Epic", "Epic description");
        SubTask subTask1 = new SubTask("SubTask1", "Description1", 1, LocalDateTime.of(2025, 3, 11, 8, 30), Duration.ofMinutes(20));
        SubTask subTask2 = new SubTask("SubTask2", "Description2", 1, LocalDateTime.of(2025, 3, 12, 8, 30), Duration.ofMinutes(20));

        // Добавляем подзадачи в Epic
        inMemoryTaskManager.saveEpic(epic);
        inMemoryTaskManager.saveSubTask(subTask1);
        inMemoryTaskManager.saveSubTask(subTask2);

        // a. Все подзадачи со статусом NEW
        assertEquals(TaskStatus.NEW, epic.getTaskStatus(), "Статус Epic должен быть NEW");

        // b. Все подзадачи со статусом DONE
        subTask1.setTaskStatus(TaskStatus.DONE);
        subTask2.setTaskStatus(TaskStatus.DONE);
        inMemoryTaskManager.updateSubTask(subTask1);
        inMemoryTaskManager.updateSubTask(subTask2);
        assertEquals(TaskStatus.DONE, epic.getTaskStatus(), "Статус Epic должен быть DONE");

        // c. Подзадачи со статусами NEW и DONE
        subTask1.setTaskStatus(TaskStatus.NEW);
        inMemoryTaskManager.updateSubTask(subTask1);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getTaskStatus(), "Статус Epic должен быть IN_PROGRESS");

        // d. Подзадачи со статусом IN_PROGRESS
        subTask1.setTaskStatus(TaskStatus.IN_PROGRESS);
        subTask2.setTaskStatus(TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.updateSubTask(subTask1);
        inMemoryTaskManager.updateSubTask(subTask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getTaskStatus(), "Статус Epic должен быть IN_PROGRESS");
    }

    @Test
    void shouldHandleEmptyHistory() {
        assertEquals(0, historyManager.getHistory().size(), "История должна быть пустой");
    }

    @Test
    void shouldHandleDuplicateTasksInHistory() {
        Task task1 = new Task("Task1", "Description1", LocalDateTime.of(2025, 3, 11, 8, 30), Duration.ofMinutes(20));
        inMemoryTaskManager.createTask(task1);

        // Добавляем задачу дважды
        historyManager.add(task1);
        historyManager.add(task1);

        assertEquals(1, historyManager.getHistory().size(), "В истории не должно быть дубликатов");
    }

    @Test
    void shouldRemoveTasksFromHistory() {
        Task task1 = new Task("Task1", "Description1", LocalDateTime.of(2025, 3, 11, 8, 30), Duration.ofMinutes(20));
        Task task2 = new Task("Task2", "Description2", LocalDateTime.of(2025, 3, 12, 8, 30), Duration.ofMinutes(20));
        Task task3 = new Task("Task3", "Description3", LocalDateTime.of(2025, 3, 13, 8, 30), Duration.ofMinutes(20));

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        // Удаляем из начала
        historyManager.remove(task1.getId());
        assertEquals(2, historyManager.getHistory().size(), "Количество элементов в истории не равно 2");

        // Удаляем из середины
        historyManager.remove(task2.getId());
        assertEquals(1, historyManager.getHistory().size(), "Количество элементов в истории не равно 1");

        // Удаляем из конца
        historyManager.remove(task3.getId());
        assertEquals(0, historyManager.getHistory().size(), "Количество элементов в истории не равно 0");
    }

    @Test
    void shouldLoadAndSaveFromFile() throws IOException, ManagerLoadException {
        File file = File.createTempFile("test-", ".csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

        Task task = new Task("Task1", "Description1", LocalDateTime.of(2025, 3, 11, 8, 30), Duration.ofMinutes(20));
        taskManager.createTask(task);

        // Сохраняем и загружаем
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        assertNotNull(loadedManager, "Менеджер не должен быть null");

        assertEquals(1, loadedManager.getAllTask().size(), "Количество задач не совпадает");
    }

    @Test
    void shouldHandleFileExceptions() {
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager taskManager = new FileBackedTaskManager(new File("/invalid/path/task.csv"));
            Task task = new Task("Task1", "Description1", LocalDateTime.of(2025, 3, 11, 8, 30), Duration.ofMinutes(20));
            taskManager.createTask(task);
        }, "Попытка сохранить файл должна приводить к ошибке");
    }
}

