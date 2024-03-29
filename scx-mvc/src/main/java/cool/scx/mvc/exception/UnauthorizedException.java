package cool.scx.mvc.exception;

import static cool.scx.common.standard.HttpStatusCode.UNAUTHORIZED;

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
        super(UNAUTHORIZED.statusCode(), UNAUTHORIZED.reasonPhrase());
    }

    /**
     * a
     *
     * @param info a
     */
    public UnauthorizedException(String info) {
        super(UNAUTHORIZED.statusCode(), UNAUTHORIZED.reasonPhrase(), info);
    }

    /**
     * a
     *
     * @param throwable a
     */
    public UnauthorizedException(Throwable throwable) {
        super(UNAUTHORIZED.statusCode(), UNAUTHORIZED.reasonPhrase(), throwable);
    }

    /**
     * a
     *
     * @param info      a
     * @param throwable a
     */
    public UnauthorizedException(String info, Throwable throwable) {
        super(UNAUTHORIZED.statusCode(), UNAUTHORIZED.reasonPhrase(), info, throwable);
    }

}
