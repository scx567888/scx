package cool.scx.vo;

import cool.scx.functional.ScxHandlerE;
import io.vertx.ext.web.RoutingContext;

/**
 * BaseVo 接口
 * 所有需要向前台返回数据都需要继承
 *
 * @author scx567888
 * @version 0.5.0
 */
public interface BaseVo extends ScxHandlerE<RoutingContext, Exception> {

}
