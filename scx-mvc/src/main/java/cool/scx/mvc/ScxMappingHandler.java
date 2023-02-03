package cool.scx.mvc;

import cool.scx.enumeration.HttpMethod;
import cool.scx.mvc.annotation.ScxMapping;
import cool.scx.util.CaseUtils;
import cool.scx.util.ScxExceptionHelper;
import cool.scx.util.URIBuilder;
import io.vertx.core.Handler;
import io.vertx.ext.web.MIMEHeader;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static cool.scx.mvc.ScxMvcHelper.responseCanUse;

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
     * scxMappingConfiguration 配置
     */
    private final ScxMvc scxMvc;

    /**
     * ScxMapping 排序 优先级最高
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
     * @param scxMvc   a
     * @param instance ex
     */
    ScxMappingHandler(Method method, Class<?> clazz, Object instance, ScxMvc scxMvc) {
        this.scxMvc = scxMvc;
        this.clazz = clazz;
        this.method = method;
        this.method.setAccessible(true);
        this.isVoid = method.getReturnType().equals(void.class);
        this.parameters = method.getParameters();
        this.instance = instance;
        //根据注解初始化值
        var classScxMapping = clazz.getAnnotation(ScxMapping.class);
        var methodScxMapping = method.getAnnotation(ScxMapping.class);
        this.originalUrl = initOriginalUrl(classScxMapping, methodScxMapping);
        this.httpMethods = initHttpMethod(methodScxMapping);
        this.order = methodScxMapping.order();
    }

    private static HttpMethod[] initHttpMethod(ScxMapping methodScxMapping) {
        return Stream.of(methodScxMapping.method()).distinct().toArray(HttpMethod[]::new);
    }

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
        ScxMvc._routingContext(context);
        try {
            //1, 执行前置处理器 (一般用于校验权限之类)
            this.scxMvc.interceptor().preHandle(context, this);
            //2, 根据 method 参数获取 invoke 时的参数
            var methodParameters = this.scxMvc.buildMethodParameters(this.parameters, context);
            //3, 执行具体方法 (用来从请求中获取参数并执行反射调用方法以获取返回值)
            var tempResult = this.method.invoke(this.instance, methodParameters);
            //4, 执行后置处理器
            var finalResult = this.scxMvc.interceptor().postHandle(context, this, tempResult);
            //5, 如果方法返回值不为 void 并且 response 可用 , 则调用返回值处理器
            if (!isVoid && responseCanUse(context)) {
                this.scxMvc.findReturnValueHandler(finalResult).handle(finalResult, context);
            }
        } catch (Throwable e) {
            //1, 如果是反射调用时发生异常 则使用反射异常的内部异常 否则使用异常
            //2, 如果是包装类型异常 (ScxWrappedRuntimeException) 则使用其内部的异常
            var exception = ScxExceptionHelper.getRootCause(e instanceof InvocationTargetException ? e.getCause() : e);
            // 注意 这里也可以直接 throw exception 并交由 VertxRouter 处理 , 但是我们直接先在内部处理掉, 防止多余的错误信息打印
            this.scxMvc.findExceptionHandler(exception).handle(exception, context);
        } finally {
            ScxMvc._clearRoutingContext();
        }
    }

    void setRouteState(RouteState route) {
        this.routeState = route;
    }

    RouteState routeState() {
        return routeState;
    }

    /**
     * <p>order.</p>
     *
     * @return a int
     */
    public int order() {
        return order;
    }

    /**
     * 用于承载数据
     */
    record RouteState(Map<String, Object> metadata, String path, String name, int order, boolean enabled,
                      Set<HttpMethod> methods, Set<MIMEHeader> consumes, boolean emptyBodyPermittedWithConsumes,
                      Set<MIMEHeader> produces, boolean added, Pattern pattern, List<String> groups,
                      boolean useNormalizedPath, Set<String> namedGroupsInRegex, Pattern virtualHostPattern,
                      boolean pathEndsWithSlash, boolean exclusive, boolean exactPath) {

        int getGroupsOrder() {
            return this.groups == null ? 0 : this.groups.size();
        }

        int getExactPathOrder() {
            return this.exactPath ? 0 : 1;
        }

    }

}
