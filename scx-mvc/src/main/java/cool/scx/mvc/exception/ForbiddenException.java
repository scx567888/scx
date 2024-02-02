package cool.scx.mvc.exception;

import static cool.scx.standard.HttpStatusCode.FORBIDDEN;

/**
 * 登录了但是没权限
 *
 * @author scx567888
 * @version 1.1.19
 */
public class ForbiddenException extends ScxHttpException {

    /**
     * a
     */
    public ForbiddenException() {
        super(FORBIDDEN.statusCode(), FORBIDDEN.reasonPhrase());
    }

    /**
     * a
     *
     * @param info a
     */
    public ForbiddenException(String info) {
        super(FORBIDDEN.statusCode(), FORBIDDEN.reasonPhrase(), info);
    }

    /**
     * a
     *
     * @param throwable a
     */
    public ForbiddenException(Throwable throwable) {
        super(FORBIDDEN.statusCode(), FORBIDDEN.reasonPhrase(), throwable);
    }

    /**
     * a
     *
     * @param info      a
     * @param throwable a
     */
    public ForbiddenException(String info, Throwable throwable) {
        super(FORBIDDEN.statusCode(), FORBIDDEN.reasonPhrase(), info, throwable);
    }

}
