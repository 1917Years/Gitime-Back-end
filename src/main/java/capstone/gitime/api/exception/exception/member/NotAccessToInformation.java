package capstone.gitime.api.exception.exception.member;

public class NotAccessToInformation extends RuntimeException {
    public NotAccessToInformation() {
        super();
    }

    public NotAccessToInformation(String message) {
        super(message);
    }

    public NotAccessToInformation(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAccessToInformation(Throwable cause) {
        super(cause);
    }

    protected NotAccessToInformation(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
