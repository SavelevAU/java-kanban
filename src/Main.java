import manager.TaskManager;
import model.Epic;
import model.Task;
import model.SubTask;
import model.TaskStatus;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("SingleTask1", "description1");
        Task task2 = new Task("SingleTask2", "description2");

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        System.out.println(taskManager.getAllTask());

        Task epic1 = new Epic("EpicTask1", "epic1 descr");
        Task epic2 = new Epic("EpicTask2", "epic2 descr");
        taskManager.saveEpic(epic1);
        taskManager.saveEpic(epic2);


        SubTask subTask1 = new SubTask("SubTask1", "des1", 3);
        SubTask subTask2 = new SubTask("SubTask2", "des2", 3);
        SubTask subTask3 = new SubTask("SubTask3", "des3", 4);

        taskManager.saveSubTask(subTask1);
        taskManager.saveSubTask(subTask2);
        taskManager.saveSubTask(subTask3);

        System.out.println("Все подзадачи:");
        System.out.println(taskManager.getAllSubtaskTask());
        System.out.println("Все ЭПИКИ:");
        System.out.println(taskManager.getAllEpic());

        task1.setTaskStatus(TaskStatus.DONE);
        System.out.println(taskManager.getTaskById(task1.getId()));

        subTask1.setTaskStatus(TaskStatus.DONE);
        taskManager.updatesubtask(subTask1);

        System.out.println("Обновили ЭПИК после обновления подзадачи:");
        System.out.println(taskManager.getEpicById(subTask1.getEpicId()));

        System.out.println("Удалили подзадачу из Эпика:");
        taskManager.deleteSubTaskById(subTask3.getId());
        System.out.println(taskManager.getEpicById(subTask3.getEpicId()));

        System.out.println("Удалили подзадачу из ЭПИКа  и пересчитали статус эпика:");
        taskManager.deleteSubTaskById(subTask1.getId());
        System.out.println(taskManager.getEpicById(subTask2.getEpicId()));
    }
}
