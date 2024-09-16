package cool.scx.http.exception;

import static cool.scx.http.HttpStatusCode.FORBIDDEN;

/**
 * 登录了但是没权限
 *
 * @author scx567888
 * @version 1.1.19
 */
public class ForbiddenException extends ScxHttpException {

    public ForbiddenException() {
        super(FORBIDDEN);
    }

    public ForbiddenException(String message) {
        super(FORBIDDEN, message);
    }

    public ForbiddenException(Throwable cause) {
        super(FORBIDDEN, cause);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(FORBIDDEN, message, cause);
    }

}
