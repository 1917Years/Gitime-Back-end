package capstone.gitime.api.exception.exception.member;

import org.springframework.security.authentication.BadCredentialsException;

public class MemberLoginException extends RuntimeException {
    public MemberLoginException() {
        super();
    }

    public MemberLoginException(String message) {
        super(message);
    }

    public MemberLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberLoginException(Throwable cause) {
        super(cause);
    }

    protected MemberLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
