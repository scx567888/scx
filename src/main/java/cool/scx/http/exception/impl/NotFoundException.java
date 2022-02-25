package cool.scx.http.exception.impl;

import cool.scx.ScxConstant;
import cool.scx.http.exception.ScxHttpException;

/**
 * 404 not found 未找到异常
 *
 * @author scx567888
 * @version 1.1.14
 */
public class NotFoundException extends ScxHttpException {

    /**
     * a
     */
    public NotFoundException() {
        super(404, ScxConstant.HTTP_NOT_FOUND_TITLE);
    }

    /**
     * a
     *
     * @param message a
     */
    public NotFoundException(String info) {
        super(404, ScxConstant.HTTP_NOT_FOUND_TITLE, info);
    }

    public NotFoundException(Throwable throwable) {
        super(404, ScxConstant.HTTP_NOT_FOUND_TITLE, throwable);
    }

}
