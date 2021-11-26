package capstone.gitime.api.exception.exception.team;

public class NotFoundTeamException extends TeamException {
    public NotFoundTeamException() {
        super();
    }

    public NotFoundTeamException(String message) {
        super(message);
    }

    public NotFoundTeamException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundTeamException(Throwable cause) {
        super(cause);
    }

    protected NotFoundTeamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
