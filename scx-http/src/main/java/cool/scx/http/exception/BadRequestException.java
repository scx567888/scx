package cool.scx.http.exception;

import static cool.scx.http.HttpStatusCode.BAD_REQUEST;

/**
 * 请求错误异常
 *
 * @author scx567888
 * @version 0.0.1
 */
public class BadRequestException extends ScxHttpException {

    public BadRequestException() {
        super(BAD_REQUEST);
    }

    public BadRequestException(String message) {
        super(BAD_REQUEST, message);
    }

    public BadRequestException(Throwable cause) {
        super(BAD_REQUEST, cause);
    }

    public BadRequestException(String message, Throwable cause) {
        super(BAD_REQUEST, message, cause);
    }

}
