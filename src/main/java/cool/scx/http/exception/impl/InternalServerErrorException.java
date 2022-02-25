package cool.scx.http.exception.impl;

import cool.scx.ScxConstant;
import cool.scx.http.exception.ScxHttpException;

/**
 * 服务器内部异常
 */
public class InternalServerErrorException extends ScxHttpException {

    public InternalServerErrorException() {
        super(500, ScxConstant.HTTP_INTERNAL_SERVER_ERROR_TITLE);
    }

    public InternalServerErrorException(String info) {
        super(500, ScxConstant.HTTP_INTERNAL_SERVER_ERROR_TITLE, info);
    }

    public InternalServerErrorException(Throwable throwable) {
        super(500, ScxConstant.HTTP_INTERNAL_SERVER_ERROR_TITLE, throwable);
    }

}
