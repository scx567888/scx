package cool.scx.mvc.exception;


import cool.scx.mvc.ScxHttpException;
import cool.scx.mvc.ScxHttpResponseStatus;

/**
 * 未认证异常 (未登录)
 *
 * @author scx567888
 * @version 1.1.19
 */
public class UnauthorizedException extends ScxHttpException {

    /**
     * a
     */
    public UnauthorizedException() {
        super(ScxHttpResponseStatus.UNAUTHORIZED.statusCode(), ScxHttpResponseStatus.UNAUTHORIZED.reasonPhrase());
    }

    /**
     * a
     *
     * @param info a
     */
    public UnauthorizedException(String info) {
        super(ScxHttpResponseStatus.UNAUTHORIZED.statusCode(), ScxHttpResponseStatus.UNAUTHORIZED.reasonPhrase(), info);
    }

    /**
     * a
     *
     * @param throwable a
     */
    public UnauthorizedException(Throwable throwable) {
        super(ScxHttpResponseStatus.UNAUTHORIZED.statusCode(), ScxHttpResponseStatus.UNAUTHORIZED.reasonPhrase(), throwable);
    }

    /**
     * a
     *
     * @param info      a
     * @param throwable a
     */
    public UnauthorizedException(String info, Throwable throwable) {
        super(ScxHttpResponseStatus.UNAUTHORIZED.statusCode(), ScxHttpResponseStatus.UNAUTHORIZED.reasonPhrase(), info, throwable);
    }

}
