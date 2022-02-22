package cool.scx.exception.impl;

import cool.scx.ScxConstant;
import cool.scx.exception.ScxHttpException;

/**
 * 未认证异常 (未登录)
 *
 * @author scx567888
 * @version 1.1.19
 */
public class UnauthorizedException extends ScxHttpException {

    public UnauthorizedException() {
        super(401, ScxConstant.HTTP_UNAUTHORIZED_TITLE, "");
    }

    public UnauthorizedException(String info) {
        super(401, ScxConstant.HTTP_UNAUTHORIZED_TITLE, info);
    }

}
