package cool.scx.http.exception;

import static cool.scx.http.HttpStatusCode.UNAUTHORIZED;

/**
 * 未认证异常 (未登录)
 *
 * @author scx567888
 * @version 1.1.19
 */
public class UnauthorizedException extends ScxHttpException {

    public UnauthorizedException() {
        super(UNAUTHORIZED);
    }

    public UnauthorizedException(String message) {
        super(UNAUTHORIZED, message);
    }

    public UnauthorizedException(Throwable cause) {
        super(UNAUTHORIZED, cause);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(UNAUTHORIZED, message, cause);
    }

}
