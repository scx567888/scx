package cool.scx.http.exception.impl;

import cool.scx.http.exception.ScxHttpException;

import static cool.scx.http.ScxHttpResponseStatus.INTERNAL_SERVER_ERROR;

/**
 * 服务器内部异常
 */
public class InternalServerErrorException extends ScxHttpException {

    public InternalServerErrorException() {
        super(INTERNAL_SERVER_ERROR.statusCode(), INTERNAL_SERVER_ERROR.reasonPhrase());
    }

    public InternalServerErrorException(String info) {
        super(INTERNAL_SERVER_ERROR.statusCode(), INTERNAL_SERVER_ERROR.reasonPhrase(), info);
    }

    public InternalServerErrorException(Throwable throwable) {
        super(INTERNAL_SERVER_ERROR.statusCode(), INTERNAL_SERVER_ERROR.reasonPhrase(), throwable);
    }

}
