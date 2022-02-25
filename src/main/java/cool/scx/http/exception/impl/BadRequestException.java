package cool.scx.http.exception.impl;

import cool.scx.ScxConstant;
import cool.scx.http.exception.ScxHttpException;

/**
 * 请求错误异常
 *
 * @author scx567888
 * @version 1.1.15
 */
public class BadRequestException extends ScxHttpException {

    /**
     * <p>Constructor for BadRequestException.</p>
     */
    public BadRequestException() {
        super(400, ScxConstant.HTTP_BAD_REQUEST_TITLE);
    }

    /**
     * <p>Constructor for BadRequestException.</p>
     *
     * @param info a {@link java.lang.Throwable} object
     */
    public BadRequestException(String info) {
        super(400, ScxConstant.HTTP_BAD_REQUEST_TITLE, info);
    }

    /**
     * <p>Constructor for BadRequestException.</p>
     *
     * @param throwable a {@link java.lang.Throwable} object
     */
    public BadRequestException(Throwable throwable) {
        super(400, ScxConstant.HTTP_BAD_REQUEST_TITLE, throwable);
    }

}
