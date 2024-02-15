public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("SingleTask1", "description1");
        Task task2 = new Task("SingleTask2", "description2");

        taskManager.saveTask(task1);
        taskManager.saveTask(task2);
        System.out.println(taskManager.getAllTask());

        Task epic1 = new epic("EpicTask1", "epic1 descr");
        Task epic2 = new epic("EpicTask2", "epic2 descr");
        taskManager.saveEpic(epic1);
        taskManager.saveEpic(epic2);


        subtask subTask1 = new subtask("SubTask1", "des1", 3);
        subtask subTask2 = new subtask("SubTask2", "des2", 3);
        subtask subTask3 = new subtask("SubTask3", "des3", 4);

        taskManager.saveSubTask(subTask1);
        taskManager.saveSubTask(subTask2);
        taskManager.saveSubTask(subTask3);

        System.out.println("Все подзадачи:");
        System.out.println(taskManager.getAllSubtaskTask());
        System.out.println("Все ЭПИКИ:");
        System.out.println(taskManager.getAllEpic());

        task1.setTaskStatus(enum_status.DONE);
        System.out.println(taskManager.getTaskByUin(task1.getUin()));

        subTask1.setTaskStatus(enum_status.DONE);
        taskManager.updatesubtask(subTask1);

        System.out.println("Обновили ЭПИК после обновления подзадачи:");
        System.out.println(taskManager.getEpicByUin(subTask1.getEpicUin()));

        System.out.println("Удалили подзадачу из Эпика:");
        taskManager.deleteSubTaskByUin(subTask3.getUin());
        System.out.println(taskManager.getEpicByUin(subTask3.getEpicUin()));

        System.out.println("Удалили подзадачу из ЭПИКа  и пересчитали статус эпика:");
        taskManager.deleteSubTaskByUin(subTask1.getUin());
        System.out.println(taskManager.getEpicByUin(subTask2.getEpicUin()));
    }
}
