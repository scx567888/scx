package cool.scx.http.exception.impl;

import cool.scx.functional.ScxHandler;
import cool.scx.http.exception.ScxHttpException;
import io.vertx.ext.web.RoutingContext;

/**
 * 自定义 HttpRequestException 异常
 *
 * @author scx567888
 * @version 1.1.14
 */
public class CustomHttpException extends ScxHttpException {

    /**
     * a
     */
    private final ScxHandler<RoutingContext> errFun;

    /**
     * 自定义异常
     *
     * @param _errFun a long.
     */
    public CustomHttpException(ScxHandler<RoutingContext> _errFun) {
        super(0, "", "");
        this.errFun = _errFun;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RoutingContext ctx) {
        errFun.handle(ctx);
    }

}
