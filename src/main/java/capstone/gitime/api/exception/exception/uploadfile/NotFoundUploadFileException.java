package capstone.gitime.api.exception.exception.uploadfile;

import capstone.gitime.api.exception.exception.global.NotFoundException;

public class NotFoundUploadFileException extends NotFoundException {
    public NotFoundUploadFileException() {
        super();
    }

    public NotFoundUploadFileException(String message) {
        super(message);
    }

    public NotFoundUploadFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundUploadFileException(Throwable cause) {
        super(cause);
    }

    protected NotFoundUploadFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
