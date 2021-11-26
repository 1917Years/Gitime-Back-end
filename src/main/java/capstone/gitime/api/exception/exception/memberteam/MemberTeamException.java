package capstone.gitime.api.exception.exception.memberteam;

public class MemberTeamException extends RuntimeException {
    public MemberTeamException() {
        super();
    }

    public MemberTeamException(String message) {
        super(message);
    }

    public MemberTeamException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberTeamException(Throwable cause) {
        super(cause);
    }

    protected MemberTeamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
