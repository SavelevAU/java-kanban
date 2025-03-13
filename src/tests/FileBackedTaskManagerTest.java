package tests;

import manager.*;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    Managers managers;
    HistoryManager historyManager;
    private TaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {

        inMemoryTaskManager = managers.getDefaultFormFile();
        historyManager = Managers.getDefaultHistory();

    }

    @Test
    void loadFromEmptyFile() throws IOException, ManagerLoadException {
        File file = File.createTempFile("testEmptyFile-", ".csv");
        FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);
        assertNotNull(taskManager, "taskManager is null!");
        assertEquals(taskManager.getAllTask().size(), 0, "Количество задач не равно 0");
        assertEquals(taskManager.getHistory().size(), 0, "Количество задач в истории не равно 0");
    }

    @Test
    void saveLoadFile() throws IOException, ManagerLoadException {
        File file = File.createTempFile("testEmptyFile-", ".csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        Task task = new Task("Test saveLoadFile", "Test saveLoadFile description", LocalDateTime.of(2025, 3,11,8,30), Duration.ofMinutes(20));
        taskManager.createTask(task);
        //вызовем получение задачи для обновления истории
        taskManager.getTaskById(task.getId());

        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(file);
        assertNotNull(taskManagerFromFile, "taskManagerFromFile is null!");
        assertEquals(taskManagerFromFile.getAllTask().size(), taskManager.getAllTask().size(), "Количество задач в менеджерах не равно");
        assertEquals(taskManagerFromFile.getHistory().size(), taskManagerFromFile.getHistory().size(), "Количество задач в истории менеджеров не равно");
    }

    @Test
    public void testException() {
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager taskManager = new FileBackedTaskManager(new File("/invalid/path/task.csv"));
            Task task = new Task("", "Test saveLoadFile description", LocalDateTime.of(2025, 3,11,8,30), Duration.ofMinutes(20));
            taskManager.createTask(task);
        }, "Попытка сохранить файл должна приводить к ошибке");
    }

    }