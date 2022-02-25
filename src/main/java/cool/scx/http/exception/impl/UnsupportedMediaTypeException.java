package cool.scx.http.exception.impl;

import cool.scx.ScxConstant;
import cool.scx.http.exception.ScxHttpException;

/**
 * 415 参数异常
 *
 * @author scx567888
 * @version 1.1.14
 */
public class UnsupportedMediaTypeException extends ScxHttpException {

    public UnsupportedMediaTypeException() {
        super(415, ScxConstant.HTTP_UNSUPPORTED_MEDIA_TYPE_TITLE);
    }

    public UnsupportedMediaTypeException(String info) {
        super(415, ScxConstant.HTTP_UNSUPPORTED_MEDIA_TYPE_TITLE, info);
    }

    public UnsupportedMediaTypeException(Throwable throwable) {
        super(415, ScxConstant.HTTP_UNSUPPORTED_MEDIA_TYPE_TITLE, throwable);
    }

}
