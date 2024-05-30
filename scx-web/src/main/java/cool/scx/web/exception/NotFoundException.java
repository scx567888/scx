package cool.scx.web.exception;

import static cool.scx.common.standard.HttpStatusCode.NOT_FOUND;

/**
 * 404 not found 未找到异常
 *
 * @author scx567888
 * @version 1.1.14
 */
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
