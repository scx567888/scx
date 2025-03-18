package cool.scx.http.exception;

import cool.scx.http.status.HttpStatusCode;

/// ScxHttpException
///
/// @author scx567888
/// @version 0.0.1
public class ScxHttpException extends RuntimeException {

    protected final HttpStatusCode statusCode;

    public ScxHttpException(HttpStatusCode statusCode) {
        super();
        this.statusCode = statusCode;
    }

    public ScxHttpException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ScxHttpException(HttpStatusCode statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    public ScxHttpException(HttpStatusCode statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public final HttpStatusCode statusCode() {
        return this.statusCode;
    }

}
