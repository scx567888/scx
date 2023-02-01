package cool.scx.mvc.exception;


import cool.scx.mvc.ScxHttpException;
import cool.scx.mvc.ScxHttpResponseStatus;

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
        super(ScxHttpResponseStatus.NOT_FOUND.statusCode(), ScxHttpResponseStatus.NOT_FOUND.reasonPhrase());
    }

    /**
     * a
     *
     * @param info a
     */
    public NotFoundException(String info) {
        super(ScxHttpResponseStatus.NOT_FOUND.statusCode(), ScxHttpResponseStatus.NOT_FOUND.reasonPhrase(), info);
    }

    /**
     * a
     *
     * @param throwable a
     */
    public NotFoundException(Throwable throwable) {
        super(ScxHttpResponseStatus.NOT_FOUND.statusCode(), ScxHttpResponseStatus.NOT_FOUND.reasonPhrase(), throwable);
    }

    /**
     * a
     *
     * @param info      a
     * @param throwable a
     */
    public NotFoundException(String info, Throwable throwable) {
        super(ScxHttpResponseStatus.NOT_FOUND.statusCode(), ScxHttpResponseStatus.NOT_FOUND.reasonPhrase(), info, throwable);
    }

}
