package capstone.gitime.api.exception.exception.memberteam;

public class NotFoundMemberTeamException extends MemberTeamException {
    public NotFoundMemberTeamException() {
        super();
    }

    public NotFoundMemberTeamException(String message) {
        super(message);
    }

    public NotFoundMemberTeamException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundMemberTeamException(Throwable cause) {
        super(cause);
    }

    protected NotFoundMemberTeamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
