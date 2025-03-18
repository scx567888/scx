package cool.scx.http.exception;

import static cool.scx.http.status.HttpStatus.CONTENT_TOO_LARGE;

/// ContentTooLargeException
///
/// @author scx567888
/// @version 0.0.1
public class ContentTooLargeException extends ScxHttpException {

    public ContentTooLargeException() {
        super(CONTENT_TOO_LARGE);
    }

    public ContentTooLargeException(String message) {
        super(CONTENT_TOO_LARGE, message);
    }

    public ContentTooLargeException(Throwable cause) {
        super(CONTENT_TOO_LARGE, cause);
    }

    public ContentTooLargeException(String message, Throwable cause) {
        super(CONTENT_TOO_LARGE, message, cause);
    }

}
