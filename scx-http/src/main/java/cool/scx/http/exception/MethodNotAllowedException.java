package cool.scx.http.exception;

import static cool.scx.http.status.HttpStatus.METHOD_NOT_ALLOWED;

/// MethodNotAllowedException
///
/// @author scx567888
/// @version 0.0.1
public class MethodNotAllowedException extends ScxHttpException {

    public MethodNotAllowedException() {
        super(METHOD_NOT_ALLOWED);
    }

    public MethodNotAllowedException(String message) {
        super(METHOD_NOT_ALLOWED, message);
    }

    public MethodNotAllowedException(Throwable cause) {
        super(METHOD_NOT_ALLOWED, cause);
    }

    public MethodNotAllowedException(String message, Throwable cause) {
        super(METHOD_NOT_ALLOWED, message, cause);
    }

}
