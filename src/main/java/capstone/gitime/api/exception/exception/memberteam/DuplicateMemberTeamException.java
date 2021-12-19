package capstone.gitime.api.exception.exception.memberteam;

public class DuplicateMemberTeamException extends RuntimeException {
    public DuplicateMemberTeamException() {
        super();
    }

    public DuplicateMemberTeamException(String message) {
        super(message);
    }

    public DuplicateMemberTeamException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateMemberTeamException(Throwable cause) {
        super(cause);
    }

    protected DuplicateMemberTeamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
