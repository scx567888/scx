package cool.scx.web;

import cool.scx.common.util.CaseUtils;
import cool.scx.common.util.ScxExceptionHelper;
import cool.scx.common.util.ScxExceptionHelper.ScxWrappedRuntimeException;
import cool.scx.common.util.URIBuilder;
import cool.scx.http.HttpMethod;
import cool.scx.http.ScxHttpMethod;
import cool.scx.http.routing.MethodMatcher;
import cool.scx.http.routing.PathMatcher;
import cool.scx.http.routing.Route;
import cool.scx.http.routing.RoutingContext;
import cool.scx.reflect.MethodInfo;
import cool.scx.web.annotation.ScxRoute;
import cool.scx.web.parameter_handler.ParameterHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.function.Consumer;

import static cool.scx.common.util.AnnotationUtils.getAnnotationValue;
import static cool.scx.web.RouteRegistrar.findScxRouteOrThrow;

/**
 * <p>ScxRouteHandler class.</p>
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class ScxRouteHandler implements Route, Consumer<RoutingContext> {

    public final MethodInfo method;
    public final boolean isVoid;
    public final Object instance;
    public final Class<?> clazz;
    private final ScxWeb scxWeb;
    private final String path;
    private final Set<HttpMethod> methods;
    private final int order;
    private final PathMatcher pathMatcher;
    private final MethodMatcher methodMatcher;
    private final ParameterHandler[] parameterHandlers;

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
        this.path = initPath(clazzAnnotation, methodAnnotation);
        this.methods = Set.of(methodAnnotation.methods());
        this.order = methodAnnotation.order();
        this.pathMatcher = path.isBlank() ? PathMatcher.any() : PathMatcher.of(path);
        this.methodMatcher = methods.isEmpty() ? MethodMatcher.any() : MethodMatcher.of(methods.toArray(ScxHttpMethod[]::new));
        this.parameterHandlers = scxWeb.buildParameterHandlers(this.method.parameters());
    }

    private String initPath(ScxRoute classAnnotation, ScxRoute methodAnnotation) {
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
            var methodParameters = this.scxWeb.buildMethodParameters(parameterHandlers, context);
            //3, 执行具体方法 (用来从请求中获取参数并执行反射调用方法以获取返回值)
            var tempResult = this.method.method().invoke(this.instance, methodParameters);
            //4, 执行后置处理器
            var finalResult = this.scxWeb.interceptor().postHandle(context, this, tempResult);
            //5, 如果方法返回值不为 void 并且 response 可用 , 则调用返回值处理器
            if (!isVoid && !context.response().isClosed()) {
                this.scxWeb.findReturnValueHandler(finalResult).handle(finalResult, context);
            }
        } catch (Throwable e) {
            //1, 如果是反射调用时发生异常 则使用反射异常的内部异常 否则使用异常
            //2, 如果是包装类型异常 (ScxWrappedRuntimeException) 则使用其内部的异常
            var exception = ScxExceptionHelper.getRootCause(e instanceof InvocationTargetException ? e.getCause() : e);
            throw new ScxWrappedRuntimeException(exception);
        }
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public Set<HttpMethod> methods() {
        return methods;
    }

    @Override
    public PathMatcher pathMatcher() {
        return pathMatcher;
    }

    @Override
    public MethodMatcher methodMatcher() {
        return methodMatcher;
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public Consumer<RoutingContext> handler() {
        return this;
    }

}
