package cool.scx.mvc;

import cool.scx.Scx;
import cool.scx.ScxContext;
import cool.scx.annotation.ScxMapping;
import cool.scx.enumeration.HttpMethod;
import cool.scx.http.ScxHttpRouter;
import cool.scx.util.CaseUtils;
import cool.scx.util.StringUtils;
import cool.scx.util.exception.ScxExceptionHelper;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * <p>ScxRouteHandler class.</p>
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class ScxMappingHandler implements Handler<RoutingContext> {

    /**
     * 用来校验 路径的正则表达式
     */
    private static final Pattern RE_TOKEN_SEARCH = Pattern.compile(":(\\w+)");

    /**
     * 方法
     */
    public final Method method;

    /**
     * 实例
     */
    public final Object example;

    /**
     * clazz 对象
     */
    public final Class<?> clazz;

    /**
     * 真实的 url 规则 是 {类注解值}/{方法注解值} 并采取简单的去除重复 "/"
     */
    public final String url;

    /**
     * 去除参数差异后的 路径
     * /api/:a 转换为 /api/?
     * /api/:b 转换为 /api/?
     * 以此来判断 两个 scxMappingHandler 的路径是否相同
     */
    public final String patternUrl;

    /**
     * httpMethods 由 注解上的 method 属性转换而来 并采用 set 进行去重
     */
    public final HttpMethod[] httpMethods;

    /**
     * scxMappingConfiguration 配置
     */
    private final ScxMappingConfiguration scxMappingConfiguration;

    /**
     * a
     */
    private final ScxHttpRouter scxHttpRouter;

    /**
     * handler 排序 目前规则是以 路径匹配参数的数量为标准
     * 如 /api/:a/:b 的参数为 2 个 /api/test/:b 的参数为 1 个 ,所以需要将 第一个路径的匹配优先级要小于第二个路径
     */
    private int order = 0;

    /**
     * a
     *
     * @param clazz         a
     * @param method        a
     * @param scx           a
     * @param scxHttpRouter a
     */
    public ScxMappingHandler(Class<?> clazz, Method method, Scx scx, ScxHttpRouter scxHttpRouter) {
        var methodScxMapping = method.getAnnotation(ScxMapping.class);
        var classScxMapping = clazz.getAnnotation(ScxMapping.class);
        this.clazz = clazz;
        this.method = method;
        this.example = scx.scxBeanFactory().getBean(clazz);
        this.url = getUrl(classScxMapping, methodScxMapping);
        this.patternUrl = getPatternUrl(this.url);
        this.httpMethods = getHttpMethod(methodScxMapping);
        this.method.setAccessible(true);
        this.scxMappingConfiguration = scx.scxMappingConfiguration();
        this.scxHttpRouter = scxHttpRouter;
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
     * <p>Getter for the field <code>url</code>.</p>
     *
     * @param classScxMapping  a {@link cool.scx.annotation.ScxMapping} object
     * @param methodScxMapping a {@link cool.scx.annotation.ScxMapping} object
     * @return a {@link java.lang.String} object
     */
    private String getUrl(ScxMapping classScxMapping, ScxMapping methodScxMapping) {
        var urlArray = new String[]{"", ""};
        if (!methodScxMapping.ignoreParentUrl() && classScxMapping != null) {
            urlArray[0] = classScxMapping.value();
        }
        //获取方法的 url
        if (methodScxMapping.useNameAsUrl() && "".equals(methodScxMapping.value())) {
            urlArray[1] = CaseUtils.toKebab(method.getName());
        } else {
            urlArray[1] = methodScxMapping.value();
        }
        return StringUtils.cleanHttpURL(urlArray);
    }

    /**
     * <p>getHttpMethod.</p>
     *
     * @param methodScxMapping a {@link cool.scx.annotation.ScxMapping} object
     * @return an array of {@link cool.scx.enumeration.HttpMethod} objects
     */
    private HttpMethod[] getHttpMethod(ScxMapping methodScxMapping) {
        return Stream.of(methodScxMapping.method()).distinct().toArray(HttpMethod[]::new);
    }

    /**
     * 获取去除路径参数的 url 主要用来判断重复路径
     *
     * @param path p
     * @return r
     */
    private String getPatternUrl(String path) {
        var m = RE_TOKEN_SEARCH.matcher(path);
        var sb = new StringBuilder();
        while (m.find()) {
            m.appendReplacement(sb, "?");
            order = order + 1;
        }
        m.appendTail(sb);
        return sb.toString();
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
            this.scxMappingConfiguration.scxMappingInterceptor().preHandle(context, this);
            //2, 根据 method 参数获取 invoke 时的参数
            var methodParameters = this.scxMappingConfiguration.buildMethodParameters(method.getParameters(), context);
            //3, 执行具体方法 (用来从请求中获取参数并执行反射调用方法以获取返回值)
            var tempResult = this.method.invoke(this.example, methodParameters);
            //4, 执行后置处理器
            var finalResult = this.scxMappingConfiguration.scxMappingInterceptor().postHandle(context, this, tempResult);
            if (!context.request().response().ended() && !context.request().response().closed()) {
                this.scxMappingConfiguration.findMethodReturnValueHandler(finalResult).handle(finalResult, context);
            }
        } catch (Throwable e) {
            //1, 如果是反射调用时发生异常 则使用反射异常的内部异常 否则使用异常
            //2, 如果是包装类型异常 (ScxWrappedRuntimeException) 则使用其内部的异常
            var exception = ScxExceptionHelper.getRootCause(e instanceof InvocationTargetException ? e.getCause() : e);
            this.scxHttpRouter.findExceptionHandler(exception).handle(exception, context);
        } finally {
            ScxContext._clearRoutingContext();
        }
    }

}
