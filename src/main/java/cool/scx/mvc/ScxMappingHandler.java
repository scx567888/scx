package cool.scx.mvc;

import cool.scx.ScxContext;
import cool.scx.ScxHandler;
import cool.scx.annotation.ScxMapping;
import cool.scx.enumeration.HttpMethod;
import cool.scx.enumeration.ScxFeature;
import cool.scx.exception.HttpRequestException;
import cool.scx.mvc.interceptor.ScxMappingInterceptorConfiguration;
import cool.scx.mvc.processor.ScxMappingResultProcessorConfiguration;
import cool.scx.util.CaseUtils;
import cool.scx.util.ExceptionUtils;
import cool.scx.util.StringUtils;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public final class ScxMappingHandler implements ScxHandler<RoutingContext> {

    private static final Logger logger = LoggerFactory.getLogger(ScxMappingHandler.class);

    /**
     * 用来校验 路径的正则表达式
     */
    private static final Pattern RE_TOKEN_SEARCH = Pattern.compile(":([A-Za-z0-9_]+)");

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
     * 方法的参数信息 比如参数名称 参数 来源(body,path等) 参数是否必填 等信息
     */
    public final ScxMappingHandlerMethodParamInfo[] paramsInfos;

    /**
     * handler 排序 目前规则是以 路径匹配参数的数量为标准
     * 如 /api/:a/:b 的参数为 2 个 /api/test/:b 的参数为 1 个 ,所以需要将 第一个路径的匹配优先级要小于第二个路径
     */
    private int order = 0;

    /**
     * <p>Constructor for ScxRouteHandler.</p>
     *
     * @param method a {@link java.lang.reflect.Method} object.
     * @param clazz  a {@link java.lang.Class} object.
     */
    public ScxMappingHandler(Class<?> clazz, Method method) {
        var methodScxMapping = method.getAnnotation(ScxMapping.class);
        var classScxMapping = clazz.getAnnotation(ScxMapping.class);
        this.clazz = clazz;
        this.method = method;
        this.example = ScxContext.beanFactory().getBean(clazz);
        this.url = getUrl(classScxMapping, methodScxMapping);
        this.patternUrl = getPatternUrl(this.url);
        this.httpMethods = getHttpMethod(methodScxMapping);
        this.paramsInfos = getFormattedParamsInfo(method);
    }

    private static ScxMappingHandlerMethodParamInfo[] getFormattedParamsInfo(Method method) {
        var parameters = method.getParameters();
        var _formattedParamsInfos = new ScxMappingHandlerMethodParamInfo[parameters.length];
        for (int i = 0; i < _formattedParamsInfos.length; i++) {
            _formattedParamsInfos[i] = new ScxMappingHandlerMethodParamInfo(parameters[i]);
        }
        return _formattedParamsInfos;
    }

    /**
     * <p>order.</p>
     *
     * @return a int
     */
    public int order() {
        return order;
    }

    private String getUrl(ScxMapping classScxMapping, ScxMapping methodScxMapping) {
        var urlArray = new String[]{"", ""};
        if (!methodScxMapping.ignoreParentUrl()) {
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
        ScxContext.routingContext(context);
        try {
            //1, 执行前置处理器 (一般用于校验权限之类)
            ScxMappingInterceptorConfiguration.interceptor().preHandle(context, this);
            //2, 执行具体方法 (用来从请求中获取参数并执行反射调用方法以获取返回值)
            var tempResult = this.method.invoke(this.example, new ScxMappingRequestParamInfo(context).convert(this.paramsInfos));
            //3, 判断返回值是否为 void 如果不是则继续处理, 否则直接终止方法运行
            if (void.class != this.method.getReturnType()) {
                //4, 执行后置处理器 (一般用来将方法返回的结果进行二次加工处理)
                var finalResult = ScxMappingInterceptorConfiguration.interceptor().postHandle(context, this, tempResult);
                //5, 检查 response 是否已经被客户端进行响应或关闭了 如果是 ,这里便不再进行后续处理
                if (!context.response().ended() && !context.response().closed()) {
                    //6, 根据不同的返回值类型自动进行处理
                    ScxMappingResultProcessorConfiguration.findResultProcessor(finalResult).handle(finalResult, context);
                }
            }
        } catch (Exception e) {
            //1, 如果是反射调用方法就使用 方法的内部异常 否则使用异常
            var exception = (e instanceof InvocationTargetException) ? e.getCause() : e;
            //2, 在此处进行对异常进行截获处理
            if (exception instanceof HttpRequestException) {
                ((HttpRequestException) exception).handle(context);
            } else {
                //3, 打印错误信息
                logger.error("执行反射调用时发生异常 !!!", exception);
                //4, 如果这时 response 还没有被关闭的话 就返回 500 错误信息
                if (!context.response().ended() && !context.response().closed()) {
                    //5, 这里根据是否开启了开发人员错误页面 进行相应的返回
                    context.response().setStatusCode(500)
                            .putHeader(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8")
                            .end(ScxContext.getFeatureState(ScxFeature.USE_DEVELOPMENT_ERROR_PAGE) ?
                                    ExceptionUtils.getCustomStackTrace(exception) : "Internal Server Error !!!");
                }
            }
        }
    }

}
