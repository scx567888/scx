package cool.scx.web;

import cool.scx.common.functional.ScxConsumer;
import cool.scx.common.reflect.MethodInfo;
import cool.scx.common.util.CaseUtils;
import cool.scx.common.util.ScxExceptionHelper;
import cool.scx.common.util.URIBuilder;
import cool.scx.http.HttpMethod;
import cool.scx.http.routing.RoutingContext;
import cool.scx.web.annotation.ScxRoute;
import io.vertx.core.Handler;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Consumer;

import static cool.scx.common.util.AnnotationUtils.getAnnotationValue;
import static cool.scx.web.RouteRegistrar.findScxRouteOrThrow;
import static cool.scx.web.ScxWebHelper.responseCanUse;

/**
 * <p>ScxRouteHandler class.</p>
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class ScxRouteHandler implements Consumer<RoutingContext> {

    /**
     * 方法
     */
    public final MethodInfo method;

    /**
     * 返回值类型为空
     */
    public final boolean isVoid;

    /**
     * 实例
     */
    public final Object instance;

    /**
     * clazz 对象
     */
    public final Class<?> clazz;

    /**
     * 原始的 url 处理规则为 {类注解值}/{方法注解值} 并采取简单的去除重复 "/"
     */
    public final String originalUrl;

    /**
     * httpMethods 由 注解上的 method 属性转换而来 并采用 set 进行去重
     */
    public final HttpMethod[] httpMethods;

    /**
     * 配置
     */
    private final ScxWeb scxWeb;

    /**
     * 排序 优先级最高
     */
    private final int order;

    /**
     * a
     */
    private RouteState routeState;

    /**
     * a
     *
     * @param method   a
     * @param scxWeb   a
     * @param instance ex
     */
    ScxRouteHandler(MethodInfo method, Object instance, ScxWeb scxWeb) {
        this.scxWeb = scxWeb;
        this.clazz = instance.getClass();
        this.method = method;
        this.method.setAccessible(true);
        this.isVoid = method.returnType().getRawClass() == void.class;
        this.instance = instance;
        //根据注解初始化值
        var clazzAnnotation = clazz.getAnnotation(ScxRoute.class);
        var methodAnnotation = findScxRouteOrThrow(method);
        this.originalUrl = initOriginalUrl(clazzAnnotation, methodAnnotation);
        this.httpMethods = distinct(methodAnnotation.methods());
        this.order = methodAnnotation.order();
    }

    private static HttpMethod[] distinct(HttpMethod[] methods) {
        return Arrays.stream(methods).distinct().toArray(HttpMethod[]::new);
    }

    private static String[] distinct(String[] strings) {
        return Arrays.stream(strings).distinct().toArray(String[]::new);
    }

    private String initOriginalUrl(ScxRoute classAnnotation, ScxRoute methodAnnotation) {
        var classUrl = "";
        var methodUrl = "";
        //处理 类 级别的注解的 url
        if (classAnnotation != null && !methodAnnotation.ignoreParentUrl()) {
            var value = getAnnotationValue(classAnnotation.value());
            if (value != null) {
                classUrl = value;
            }
        }
        //处理 方法 级别的注解的 url
        var value = getAnnotationValue(methodAnnotation.value());
        if (value != null) {
            methodUrl = value;
        } else if (methodAnnotation.useNameAsUrl()) {
            methodUrl = CaseUtils.toKebab(this.method.name());
        }
        return URIBuilder.addSlashStart(URIBuilder.join(classUrl, methodUrl));
    }

    @Override
    public void accept(RoutingContext context) {
        //0, 将 routingContext 注入到 ThreadLocal 中去 以方便后续从静态方法调用
        ScxWeb._routingContext(context);
        try {
            //1, 执行前置处理器 (一般用于校验权限之类)
            this.scxWeb.interceptor().preHandle(context, this);
            //2, 根据 method 参数获取 invoke 时的参数
            var methodParameters = this.scxWeb.buildMethodParameters(this.method.parameters(), context);
            //3, 执行具体方法 (用来从请求中获取参数并执行反射调用方法以获取返回值)
            var tempResult = this.method.method().invoke(this.instance, methodParameters);
            //4, 执行后置处理器
            var finalResult = this.scxWeb.interceptor().postHandle(context, this, tempResult);
            //5, 如果方法返回值不为 void 并且 response 可用 , 则调用返回值处理器
            if (!isVoid && responseCanUse(context)) {
                this.scxWeb.findReturnValueHandler(finalResult).handle(finalResult, context);
            }
        } catch (Throwable e) {
            //1, 如果是反射调用时发生异常 则使用反射异常的内部异常 否则使用异常
            //2, 如果是包装类型异常 (ScxWrappedRuntimeException) 则使用其内部的异常
            var exception = ScxExceptionHelper.getRootCause(e instanceof InvocationTargetException ? e.getCause() : e);
            // 注意 这里也可以直接 throw exception 并交由 VertxRouter 处理 , 但是我们直接先在内部处理掉, 防止多余的错误信息打印
            this.scxWeb.findExceptionHandler(exception).handle(exception, context);
        } finally {
            ScxWeb._clearRoutingContext();
        }
    }

    void setRouteState(RouteState route) {
        this.routeState = route;
    }

    RouteState routeState() {
        return routeState;
    }

    public int order() {
        return order;
    }

}
