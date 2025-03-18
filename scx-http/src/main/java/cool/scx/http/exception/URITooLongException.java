package cool.scx.http.exception;

import static cool.scx.http.status.HttpStatus.URI_TOO_LONG;

/// URITooLongException
///
/// @author scx567888
/// @version 0.0.1
public class URITooLongException extends ScxHttpException {

    public URITooLongException() {
        super(URI_TOO_LONG);
    }

    public URITooLongException(String message) {
        super(URI_TOO_LONG, message);
    }

    public URITooLongException(Throwable cause) {
        super(URI_TOO_LONG, cause);
    }

    public URITooLongException(String message, Throwable cause) {
        super(URI_TOO_LONG, message, cause);
    }

}
