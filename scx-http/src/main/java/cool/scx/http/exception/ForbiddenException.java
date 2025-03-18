package cool.scx.http.exception;

import static cool.scx.http.status.HttpStatusCode.FORBIDDEN;

/// ForbiddenException
///
/// @author scx567888
/// @version 0.0.1
public class ForbiddenException extends ScxHttpException {

    public ForbiddenException() {
        super(FORBIDDEN);
    }

    public ForbiddenException(String message) {
        super(FORBIDDEN, message);
    }

    public ForbiddenException(Throwable cause) {
        super(FORBIDDEN, cause);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(FORBIDDEN, message, cause);
    }

}
