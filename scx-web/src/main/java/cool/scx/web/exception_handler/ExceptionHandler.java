package cool.scx.web.exception_handler;

import cool.scx.http.routing.RoutingContext;

/// 路由异常处理器
///
/// @author scx567888
/// @version 0.0.1
public interface ExceptionHandler {

    /// 判断是否可以处理这个异常
    boolean canHandle(Throwable throwable);

    /// 将结果处理并返回
    void handle(Throwable throwable, RoutingContext routingContext);

}
