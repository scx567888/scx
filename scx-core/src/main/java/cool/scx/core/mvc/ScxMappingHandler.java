package cool.scx.core.mvc;

import cool.scx.core.Scx;
import cool.scx.core.ScxContext;
import cool.scx.core.annotation.ScxMapping;
import cool.scx.enumeration.HttpMethod;
import cool.scx.util.CaseUtils;
import cool.scx.util.ScxExceptionHelper;
import cool.scx.util.URIBuilder;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

import static cool.scx.core.ScxHelper.responseCanUse;

/**
 * <p>ScxRouteHandler class.</p>
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class ScxMappingHandler implements Handler<RoutingContext> {

    /**
     * 方法
     */
    public final Method method;

    /**
     * 返回值类型为空
     */
    public final boolean isVoid;

    /**
     * 方法参数
     */
    public final Parameter[] parameters;

    /**
     * 实例
     */
    public final Object example;

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
     * scxMappingConfiguration 配置
     */
    private final Scx scx;

    /**
     * ScxMapping 排序 优先级最高
     */
    private final int order;

    /**
     * a
     */
    private ScxMappingRegistrar.RouteState routeState;

    /**
     * a
     *
     * @param clazz  a
     * @param method a
     * @param scx    a
     */
    public ScxMappingHandler(Class<?> clazz, Method method, Scx scx) {
        this.scx = scx;
        this.clazz = clazz;
        this.method = method;
        this.method.setAccessible(true);
        this.isVoid = method.getReturnType().equals(void.class);
        this.parameters = method.getParameters();
        this.example = this.scx.scxBeanFactory().getBean(clazz);
        //根据注解初始化值
        var classScxMapping = clazz.getAnnotation(ScxMapping.class);
        var methodScxMapping = method.getAnnotation(ScxMapping.class);
        this.originalUrl = initOriginalUrl(classScxMapping, methodScxMapping);
        this.httpMethods = initHttpMethod(methodScxMapping);
        this.order = methodScxMapping.order();
    }

    /**
     * <p>getHttpMethod.</p>
     *
     * @param methodScxMapping a {@link cool.scx.core.annotation.ScxMapping} object
     * @return an array of {@link cool.scx.enumeration.HttpMethod} objects
     */
    private static HttpMethod[] initHttpMethod(ScxMapping methodScxMapping) {
        return Stream.of(methodScxMapping.method()).distinct().toArray(HttpMethod[]::new);
    }

    /**
     * <p>Getter for the field <code>url</code>.</p>
     *
     * @param classScxMapping  a {@link cool.scx.core.annotation.ScxMapping} object
     * @param methodScxMapping a {@link cool.scx.core.annotation.ScxMapping} object
     * @return a {@link java.lang.String} object
     */
    private String initOriginalUrl(ScxMapping classScxMapping, ScxMapping methodScxMapping) {
        var urlArray = new String[]{"", ""};
        if (!methodScxMapping.ignoreParentUrl() && classScxMapping != null) {
            urlArray[0] = classScxMapping.value();
        }
        //获取方法的 url
        if (methodScxMapping.useNameAsUrl() && "".equals(methodScxMapping.value())) {
            urlArray[1] = CaseUtils.toKebab(this.method.getName());
        } else {
            urlArray[1] = methodScxMapping.value();
        }
        return URIBuilder.addSlashStart(URIBuilder.join(urlArray));
    }

    /**
     * {@inheritDoc}
     * <p>
     * handle
     */
    @Override
    public void handle(RoutingContext context) {
        //0, 将 routingContext 注入到 ThreadLocal 中去 以方便后续从静态方法调用
        ScxContext._routingContext(context);
        try {
            //1, 执行前置处理器 (一般用于校验权限之类)
            this.scx.scxMappingConfiguration().scxMappingInterceptor().preHandle(context, this);
            //2, 根据 method 参数获取 invoke 时的参数
            var methodParameters = this.scx.scxMappingConfiguration().buildMethodParameters(this.parameters, context);
            //3, 执行具体方法 (用来从请求中获取参数并执行反射调用方法以获取返回值)
            var tempResult = this.method.invoke(this.example, methodParameters);
            //4, 执行后置处理器
            var finalResult = this.scx.scxMappingConfiguration().scxMappingInterceptor().postHandle(context, this, tempResult);
            //5, 如果方法返回值不为 void 并且 response 可用 , 则调用返回值处理器
            if (!isVoid && responseCanUse(context)) {
                this.scx.scxMappingConfiguration().findMethodReturnValueHandler(finalResult).handle(finalResult, context);
            }
        } catch (Throwable e) {
            //1, 如果是反射调用时发生异常 则使用反射异常的内部异常 否则使用异常
            //2, 如果是包装类型异常 (ScxWrappedRuntimeException) 则使用其内部的异常
            var exception = ScxExceptionHelper.getRootCause(e instanceof InvocationTargetException ? e.getCause() : e);
            this.scx.scxHttpRouter().findExceptionHandler(exception).handle(exception, context);
        } finally {
            ScxContext._clearRoutingContext();
        }
    }

    /**
     * <p>Setter for the field <code>routeState</code>.</p>
     *
     * @param route a {@link cool.scx.core.mvc.ScxMappingRegistrar.RouteState} object
     */
    void setRouteState(ScxMappingRegistrar.RouteState route) {
        this.routeState = route;
    }

    /**
     * <p>routeState.</p>
     *
     * @return a {@link cool.scx.core.mvc.ScxMappingRegistrar.RouteState} object
     */
    ScxMappingRegistrar.RouteState routeState() {
        return routeState;
    }

    /**
     * <p>order.</p>
     *
     * @return a int
     */
    int order() {
        return order;
    }

}
