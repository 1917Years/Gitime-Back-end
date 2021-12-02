package capstone.gitime.api.exception.exception.developfield;

import capstone.gitime.api.exception.exception.global.NotFoundException;

public class NotFoundDevelopFieldException extends NotFoundException {
    public NotFoundDevelopFieldException() {
        super();
    }

    public NotFoundDevelopFieldException(String message) {
        super(message);
    }

    public NotFoundDevelopFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundDevelopFieldException(Throwable cause) {
        super(cause);
    }

    protected NotFoundDevelopFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
