package capstone.gitime.api.exception.exception.board;

import capstone.gitime.api.exception.exception.global.NotFoundException;

public class NotFoundBoardException extends NotFoundException {
    public NotFoundBoardException() {
        super();
    }

    public NotFoundBoardException(String message) {
        super(message);
    }

    public NotFoundBoardException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundBoardException(Throwable cause) {
        super(cause);
    }

    protected NotFoundBoardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
