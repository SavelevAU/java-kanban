package model;

public class TaskIdGenerator {
    private int newId = 0;
//
    public int getNewId() {
        return ++newId;
    }
}
