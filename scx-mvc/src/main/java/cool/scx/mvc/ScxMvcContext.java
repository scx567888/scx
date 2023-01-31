package cool.scx.mvc;

import io.vertx.ext.web.RoutingContext;

public class ScxMvcContext {

    /**
     * 路由上下文 THREAD_LOCAL
     */
    private static final InheritableThreadLocal<RoutingContext> ROUTING_CONTEXT_THREAD_LOCAL = new InheritableThreadLocal<>();

    /**
     * 获取当前线程的 RoutingContext (只限在 scx mapping 注解的方法及其调用链上)
     *
     * @return 当前线程的 RoutingContext
     */
    public static RoutingContext routingContext() {
        return ROUTING_CONTEXT_THREAD_LOCAL.get();
    }

    /**
     * 设置当前线程的 routingContext
     * 此方法正常之给 scxMappingHandler 调用
     * 若无特殊需求 不必调用此方法
     *
     * @param routingContext 要设置的 routingContext
     */
    public static void _routingContext(RoutingContext routingContext) {
        ROUTING_CONTEXT_THREAD_LOCAL.set(routingContext);
    }

    /**
     * a
     */
    public static void _clearRoutingContext() {
        ROUTING_CONTEXT_THREAD_LOCAL.remove();
    }

}
