package capstone.gitime.api.exception.exception.todo;

import capstone.gitime.api.exception.exception.global.NotFoundException;

public class NotFoundTodoException extends NotFoundException {
    public NotFoundTodoException() {
        super();
    }

    public NotFoundTodoException(String message) {
        super(message);
    }

    public NotFoundTodoException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundTodoException(Throwable cause) {
        super(cause);
    }

    protected NotFoundTodoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
