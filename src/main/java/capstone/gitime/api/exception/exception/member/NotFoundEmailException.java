package capstone.gitime.api.exception.exception.member;

public class NotFoundEmailException extends MemberLoginException {
    public NotFoundEmailException() {
        super();
    }

    public NotFoundEmailException(String message) {
        super(message);
    }

    public NotFoundEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundEmailException(Throwable cause) {
        super(cause);
    }

    protected NotFoundEmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
