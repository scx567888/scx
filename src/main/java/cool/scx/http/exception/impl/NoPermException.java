package cool.scx.http.exception.impl;

import cool.scx.ScxConstant;
import cool.scx.http.exception.ScxHttpException;

/**
 * 登录了但是没权限
 *
 * @author scx567888
 * @version 1.1.19
 */
public class NoPermException extends ScxHttpException {

    public NoPermException() {
        super(403, ScxConstant.HTTP_NO_PERM_TITLE);
    }

    public NoPermException(String info) {
        super(403, ScxConstant.HTTP_NO_PERM_TITLE, info);
    }

    public NoPermException(Throwable throwable) {
        super(403, ScxConstant.HTTP_NO_PERM_TITLE, throwable);
    }

}
