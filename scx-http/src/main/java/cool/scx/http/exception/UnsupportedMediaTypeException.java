package cool.scx.http.exception;

import static cool.scx.http.status.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

/// UnsupportedMediaTypeException
///
/// @author scx567888
/// @version 0.0.1
public class UnsupportedMediaTypeException extends ScxHttpException {

    public UnsupportedMediaTypeException() {
        super(UNSUPPORTED_MEDIA_TYPE);
    }

    public UnsupportedMediaTypeException(String message) {
        super(UNSUPPORTED_MEDIA_TYPE, message);
    }

    public UnsupportedMediaTypeException(Throwable cause) {
        super(UNSUPPORTED_MEDIA_TYPE, cause);
    }

    public UnsupportedMediaTypeException(String message, Throwable cause) {
        super(UNSUPPORTED_MEDIA_TYPE, message, cause);
    }

}
