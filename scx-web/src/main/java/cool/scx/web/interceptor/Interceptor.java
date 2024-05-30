package cool.scx.web.interceptor;

import cool.scx.web.ScxRouteHandler;
import io.vertx.ext.web.RoutingContext;

/**
 * 拦截器
 *
 * @author scx567888
 * @version 1.3.14
 */
public interface Interceptor {

    /**
     * 前置处理器 在 ScxMappingHandler 所对应的方法执行前调用
     * 用来进行权限验证等操作  若要中断执行请在 handler 中抛出异常 ,异常会有 ScxMappingHandler 的异常处理器进行处理
     *
     * @param routingContext  ctx
     * @param scxRouteHandler ScxMappingHandler 实例
     * @throws java.lang.Exception 异常
     */
    default void preHandle(RoutingContext routingContext, ScxRouteHandler scxRouteHandler) throws Exception {

    }

    /**
     * 注意 : 若处理器中的方法 返回值为 void (即无返回值) 此拦截器则不会执行
     * 后置处理器 在 ScxMappingHandler 所对应的方法执行完成之后 但是并没有将结果响应回客户端之前调用
     * 可再次对响应的数据进行修改
     *
     * @param routingContext  ctx 上下文对象
     * @param scxRouteHandler 待处理的 scxMappingHandler
     * @param result          上一步 ScxMappingHandler 核心处理器 处理返回的结果
     * @return 处理后的结果
     * @throws java.lang.Exception java.lang.Exception 会交给 ScxMappingExceptionProcessor 进行处理
     */
    default Object postHandle(RoutingContext routingContext, ScxRouteHandler scxRouteHandler, Object result) throws Exception {
        return result;
    }

}
