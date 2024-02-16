package model;

public class TaskIdGenerator {
    private static int newId = 0;

    public int getNewId() {
        return ++newId;
    }
}
