package manager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {

        return new InMemoryTaskManager();
    }

    public static TaskManager getDefaultFormFile() {

        return new FileBackedTaskManager(new File("resources/task.csv"));
    }
    public static HistoryManager getDefaultHistory() {

        return new InMemoryHistoryManager();
    }
}
//