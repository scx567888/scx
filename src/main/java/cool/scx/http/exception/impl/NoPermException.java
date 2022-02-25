package cool.scx.http.exception.impl;

import cool.scx.http.exception.ScxHttpException;

import static cool.scx.http.ScxHttpResponseStatus.NO_PERM;

/**
 * 登录了但是没权限
 *
 * @author scx567888
 * @version 1.1.19
 */
public class NoPermException extends ScxHttpException {

    public NoPermException() {
        super(NO_PERM.statusCode(), NO_PERM.reasonPhrase());
    }

    public NoPermException(String info) {
        super(NO_PERM.statusCode(), NO_PERM.reasonPhrase(), info);
    }

    public NoPermException(Throwable throwable) {
        super(NO_PERM.statusCode(), NO_PERM.reasonPhrase(), throwable);
    }

}
