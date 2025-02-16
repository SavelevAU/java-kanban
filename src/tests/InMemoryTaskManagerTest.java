package tests;
import org.junit.jupiter.api.BeforeEach;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {
    Managers managers;
    HistoryManager historyManager;
    private TaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {

        inMemoryTaskManager = managers.getDefault();
        historyManager = inMemoryTaskManager.getHistoryManager();

    }

    @Test
    void shouldCheckThatInMemoryManagerAddTasksAndFindThemByById() {

        Task task1 = new Task("Task1", "description1");
        inMemoryTaskManager.saveTask(task1);
        Task task = inMemoryTaskManager.getTaskById(1);
        assertEquals(1, task.getId());
    }

    @Test
    void shouldSavePreviousVersionOfTaskAndData() {
        Task task1 = new Task("Task1", "description1");
        inMemoryTaskManager.saveTask(task1);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(1);
        assertEquals(1, inMemoryTaskManager.getHistory().size());
    }
    //убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    void TheUtilityClassReturnsInitializedInstancesManagers() {
        TaskManager taskManager = Managers.getDefault();
        Assertions.assertNotNull(taskManager);

        HistoryManager historyManager = Managers.getDefaultHistory();
        Assertions.assertNotNull(historyManager);

    }

    @Test
    void AddsTasksDifferentTypesAndCanFindThemByManagerId(){
        Task task1 = new Task("Task1", "description1");
        inMemoryTaskManager.saveTask(task1);
        Task task2 = new Epic("Epic1", "description1");
        inMemoryTaskManager.saveEpic(task2);
        Task singTask = inMemoryTaskManager.getTaskById(task1.getId());
        Assertions.assertNotNull(singTask);
        Task epicTask = inMemoryTaskManager.getEpicById(task2.getId());
        Assertions.assertNotNull(epicTask);
        Assertions.assertNotEquals(singTask, epicTask);
    }
//

    @Test
    void TasksWithGivenIdAndGeneratedDoNotConflictWithinTheManager(){
        Task task1 = new Task(1000,"Task1","description1",  TaskStatus.NEW);
        inMemoryTaskManager.updateTask(task1);
        Task task2 = new Task("Task2", "description2");
        inMemoryTaskManager.saveTask(task2);
        Assertions.assertFalse(task1.getId() == task2.getId());

    }

    @Test
    void shouldRemoveFromHistory(){
        Task task1 = new Task("Task1", "description1");
        inMemoryTaskManager.createTask(task1);
        historyManager.add(task1);
        Task task2 = new Task("Task2", "description2");
        inMemoryTaskManager.createTask(task2);
        historyManager.add(task2);
        Task task3 = new Task("Task3", "description3");
        inMemoryTaskManager.createTask(task3);
        historyManager.add(task3);

        historyManager.remove(task2.getId());
        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "Количество элементов в истории не равно 2");
        assertEquals(task1, history.get(0), "Первый элемент истории определен неверно");
        assertEquals(task3, history.get(1), "Последний элемент истории определен неверно");

        historyManager.remove(task1.getId());
        final List<Task> historyAfterRemoveFirst = historyManager.getHistory();
        assertEquals(1, historyAfterRemoveFirst.size(), "Количество элементов в истории не равно 1");
        assertEquals(task3, historyAfterRemoveFirst.get(0), "Элемент истории определен неверно");

        historyManager.remove(task3.getId());
        final List<Task> historyAfterRemoveAll = historyManager.getHistory();
        assertEquals(0, historyAfterRemoveAll.size(), "Количество элементов в истории не равно 0");
    }
}