package manager;
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

class InMemoryTaskManagerTest {
    Managers managers;
    private TaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {

        inMemoryTaskManager = managers.getDefault();

    }

    @Test
    void shouldCheckThatInMemoryManagerAddTasksAndFindThemByById() {

        Task task1 = new Task("Task1", "description1");
        inMemoryTaskManager.saveTask(task1);
        Task task = inMemoryTaskManager.getTaskById(1);
        Assertions.assertEquals(1, task.getId());
    }

    @Test
    void shouldSavePreviousVersionOfTaskAndData() {
        Task task1 = new Task("Task1", "description1");
        inMemoryTaskManager.saveTask(task1);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(1);
        Assertions.assertEquals(2, inMemoryTaskManager.getHistory().size());
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
}