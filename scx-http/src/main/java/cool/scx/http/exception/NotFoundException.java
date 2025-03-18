package cool.scx.http.exception;

import static cool.scx.http.status.HttpStatusCode.NOT_FOUND;

/// NotFoundException
///
/// @author scx567888
/// @version 0.0.1
public class NotFoundException extends ScxHttpException {

    public NotFoundException() {
        super(NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(NOT_FOUND, message);
    }

    public NotFoundException(Throwable cause) {
        super(NOT_FOUND, cause);
    }

    public NotFoundException(String message, Throwable cause) {
        super(NOT_FOUND, message, cause);
    }

}
