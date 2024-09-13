package cool.scx.web.return_value_handler;

import cool.scx.http.routing.RoutingContext;
import cool.scx.web.vo.BaseVo;

/**
 * BaseVo 处理器
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class BaseVoReturnValueHandler implements ReturnValueHandler {

    @Override
    public boolean canHandle(Object returnValue) {
        return returnValue instanceof BaseVo;
    }

    @Override
    public void handle(Object returnValue, RoutingContext routingContext) throws Exception {
        if (returnValue instanceof BaseVo baseVo) {
            baseVo.accept(routingContext);
        } else {
            throw new IllegalArgumentException("参数不是 BaseVo 类型 !!! " + returnValue.getClass());
        }
    }

}
