package cool.scx.http.exception.impl;

import cool.scx.http.exception.ScxHttpException;

import static cool.scx.http.ScxHttpResponseStatus.UNAUTHORIZED;

/**
 * 未认证异常 (未登录)
 *
 * @author scx567888
 * @version 1.1.19
 */
public class UnauthorizedException extends ScxHttpException {

    public UnauthorizedException() {
        super(UNAUTHORIZED.statusCode(), UNAUTHORIZED.reasonPhrase());
    }

    public UnauthorizedException(String info) {
        super(UNAUTHORIZED.statusCode(), UNAUTHORIZED.reasonPhrase(), info);
    }

    public UnauthorizedException(Throwable throwable) {
        super(UNAUTHORIZED.statusCode(), UNAUTHORIZED.reasonPhrase(), throwable);
    }

}
