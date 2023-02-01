package cool.scx.mvc.exception;

import cool.scx.mvc.ScxHttpException;

import static cool.scx.mvc.ScxHttpResponseStatus.BAD_REQUEST;

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
        super(BAD_REQUEST.statusCode(), BAD_REQUEST.reasonPhrase());
    }

    /**
     * <p>Constructor for BadRequestException.</p>
     *
     * @param info a {@link java.lang.Throwable} object
     */
    public BadRequestException(String info) {
        super(BAD_REQUEST.statusCode(), BAD_REQUEST.reasonPhrase(), info);
    }

    /**
     * <p>Constructor for BadRequestException.</p>
     *
     * @param throwable a {@link java.lang.Throwable} object
     */
    public BadRequestException(Throwable throwable) {
        super(BAD_REQUEST.statusCode(), BAD_REQUEST.reasonPhrase(), throwable);
    }

    /**
     * a
     *
     * @param info      a
     * @param throwable a
     */
    public BadRequestException(String info, Throwable throwable) {
        super(BAD_REQUEST.statusCode(), BAD_REQUEST.reasonPhrase(), info, throwable);
    }

}
