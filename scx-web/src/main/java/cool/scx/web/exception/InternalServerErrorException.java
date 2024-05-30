package cool.scx.web.exception;

import static cool.scx.common.standard.HttpStatusCode.INTERNAL_SERVER_ERROR;

/**
 * 服务器内部异常
 *
 * @author scx567888
 * @version 1.11.8
 */
public class InternalServerErrorException extends ScxHttpException {

    public InternalServerErrorException() {
        super(INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorException(String message) {
        super(INTERNAL_SERVER_ERROR, message);
    }

    public InternalServerErrorException(Throwable cause) {
        super(INTERNAL_SERVER_ERROR, cause);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(INTERNAL_SERVER_ERROR, message, cause);
    }

}
