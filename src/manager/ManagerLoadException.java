package manager;

public class ManagerLoadException extends Throwable {
    public ManagerLoadException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ManagerLoadException(final String message) {
        super(message);
    }
}
