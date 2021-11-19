package cool.scx.exception;

import cool.scx.ScxHandler;
import io.vertx.ext.web.RoutingContext;

/**
 * 在 ScxMapping 注解标记的方法中抛出此异常会被ScxMappingHandler 进行截获并调用其中的 {@link #handle(Object)}
 * <p>
 * 当我们的代码中有需要向客户端返回错误信息的时候
 * <p>
 * 推荐创建 HttpRequestException 的实现类并抛出异常 , 而不是手动进行异常的处理与响应的返回
 *
 * @author scx567888
 * @version 1.0.10
 */
public abstract class ScxHttpException extends RuntimeException implements ScxHandler<RoutingContext> {

}
