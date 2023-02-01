package cool.scx.mvc.exception;


import cool.scx.mvc.ScxHttpException;
import cool.scx.mvc.ScxHttpResponseStatus;

/**
 * 服务器内部异常
 *
 * @author scx567888
 * @version 1.11.8
 */
public class InternalServerErrorException extends ScxHttpException {

    /**
     * a
     */
    public InternalServerErrorException() {
        super(ScxHttpResponseStatus.INTERNAL_SERVER_ERROR.statusCode(), ScxHttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase());
    }

    /**
     * a
     *
     * @param info a
     */
    public InternalServerErrorException(String info) {
        super(ScxHttpResponseStatus.INTERNAL_SERVER_ERROR.statusCode(), ScxHttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), info);
    }

    /**
     * a
     *
     * @param throwable a
     */
    public InternalServerErrorException(Throwable throwable) {
        super(ScxHttpResponseStatus.INTERNAL_SERVER_ERROR.statusCode(), ScxHttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), throwable);
    }

    /**
     * a
     *
     * @param info      a
     * @param throwable a
     */
    public InternalServerErrorException(String info, Throwable throwable) {
        super(ScxHttpResponseStatus.INTERNAL_SERVER_ERROR.statusCode(), ScxHttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), info, throwable);
    }

}
