package capstone.gitime.api.exception.exception.memberteam;

import capstone.gitime.api.exception.exception.global.NotFoundException;

public class NotFoundAcceptCodeException extends NotFoundException {
    public NotFoundAcceptCodeException() {
        super();
    }

    public NotFoundAcceptCodeException(String message) {
        super(message);
    }

    public NotFoundAcceptCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundAcceptCodeException(Throwable cause) {
        super(cause);
    }

    protected NotFoundAcceptCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
