public class TaskUinGenerator {
    private static int newUin = 0;

    public int getNewUin() {
        return ++newUin;
    }
}
