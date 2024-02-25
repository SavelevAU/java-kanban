package manager;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final LinkedList<Task> historyList = new LinkedList<>();
    private static final int MAX_SIZE_OF_HISTORY_LIST = 10;

    @Override
    public void add(Task task) {
        if (task != null) {
            if (historyList.size() == MAX_SIZE_OF_HISTORY_LIST) {
                historyList.removeFirst();
                historyList.add(task);
            } else {
                historyList.add(task);
            }
        }
    }
    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
