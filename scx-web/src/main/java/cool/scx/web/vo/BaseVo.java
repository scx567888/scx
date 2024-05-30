package cool.scx.web.vo;

import cool.scx.common.functional.ScxConsumer;
import io.vertx.ext.web.RoutingContext;

/**
 * BaseVo 接口
 *
 * @author scx567888
 * @version 0.5.0
 */
public interface BaseVo extends ScxConsumer<RoutingContext, Exception> {

}
