import manager.Managers;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Task;
import model.SubTask;
import model.TaskStatus;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = new Task("SingleTask1", "description1");
        Task task2 = new Task("SingleTask2", "description2");

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        System.out.println(inMemoryTaskManager.getAllTask());

        Task epic1 = new Epic("EpicTask1", "epic1 descr");
        Task epic2 = new Epic("EpicTask2", "epic2 descr");
        inMemoryTaskManager.saveEpic(epic1);
        inMemoryTaskManager.saveEpic(epic2);


        SubTask subTask1 = new SubTask("SubTask1", "des1", 3);
        SubTask subTask2 = new SubTask("SubTask2", "des2", 3);
        SubTask subTask3 = new SubTask("SubTask3", "des3", 4);

        inMemoryTaskManager.saveSubTask(subTask1);
        inMemoryTaskManager.saveSubTask(subTask2);
        inMemoryTaskManager.saveSubTask(subTask3);

        System.out.println("Все подзадачи:");
        System.out.println(inMemoryTaskManager.getAllSubtaskTask());
        System.out.println("Все ЭПИКИ:");
        System.out.println(inMemoryTaskManager.getAllEpic());

        task1.setTaskStatus(TaskStatus.DONE);

        subTask1.setTaskStatus(TaskStatus.DONE);
        inMemoryTaskManager.updatesubtask(subTask1);

        System.out.println("Обновили ЭПИК после обновления подзадачи:");

        System.out.println("Удалили подзадачу из Эпика:");
        inMemoryTaskManager.deleteSubTaskById(subTask3.getId());

        System.out.println("Удалили подзадачу из ЭПИКа  и пересчитали статус эпика:");
        inMemoryTaskManager.deleteSubTaskById(subTask1.getId());
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getEpicById(4);
        inMemoryTaskManager.getSubTaskById(6);

        printAllTasks(inMemoryTaskManager);
    }
    private static void printAllTasks(TaskManager manager) {

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
