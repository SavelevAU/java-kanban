package manager;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{

    private  static  class  Node {
        Task task;
        Node prev;
        Node next;

        private Node(Task task, Node prev, Node next){
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }


    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private  Node first;
    private  Node last;

    private ArrayList<Task> getTasks(){
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = last;
        if(node != null) {
            tasks.add(node.task);
            while (node.prev != null) {
                node = node.prev;
                tasks.add(node.task);
            }
        }

        return tasks;
    }
    private void linkLast(Task task){
        final Node node = new Node(task,last, null);
        last = node;
    }

    private final LinkedList<Task> historyList = new LinkedList<>();
    private static final int MAX_SIZE_OF_HISTORY_LIST = 10;
    @Override
    public void add(Task task) {
        remove(task.getId());
        Node node = new Node(task, null,null);
        if (first == null) {
            first = node;
            last = node;
        } else {
            last.next = node;
            node.prev = last;
            last = node;
        }
        nodeMap.put(task.getId(),node);
    }

    @Override
    public void remove(int id) {
        final Node node = nodeMap.remove(id);
        if (node != null) {
            if (node.prev == null) {
                first = node.next;
                if(node.next != null) {
                    node.next.prev = null;
                }
            } else if (node.prev != null && node.next != null){
                node.prev.next = node.next;
                node.next.prev = node.prev;
            } else {
                last = node.prev;
                if (node.prev != null){
                    node.prev.next = null;
                }
            }
        }
        if (first == null) {
            last = null;
        }

    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
