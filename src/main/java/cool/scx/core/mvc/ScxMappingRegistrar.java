package cool.scx.core.mvc;

import cool.scx.core.Scx;
import cool.scx.core.annotation.ScxMapping;
import cool.scx.util.MultiMap;
import cool.scx.util.ObjectUtils;
import cool.scx.util.ClassUtils;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.MIMEHeader;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.impl.RouteImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>ScxMappingRegistrar class.</p>
 *
 * @author scx567888
 * @version 1.17.8
 */
public final class ScxMappingRegistrar {

    /**
     * Constant <code>logger</code>
     */
    private static final Logger logger = LoggerFactory.getLogger(ScxMappingRegistrar.class);

    /**
     * Constant <code>vertxRouteStateMethod</code>
     */
    private static final Method vertxRouteStateMethod = initVertxRouteStateMethod();

    /**
     * Constant <code>exactPathComparator</code>
     */
    private static final Comparator<ScxMappingHandler> exactPathComparator = Comparator.comparing(routeState -> routeState.routeState().getExactPathOrder());

    /**
     * Constant <code>groupsComparator</code>
     */
    private static final Comparator<ScxMappingHandler> groupsComparator = Comparator.comparing(routeState -> routeState.routeState().getGroupsOrder());

    /**
     * Constant <code>orderComparator</code>
     */
    private static final Comparator<ScxMappingHandler> orderComparator = Comparator.comparing(ScxMappingHandler::order);

    /**
     * Constant <code>RE_TOKEN_SEARCH</code>
     */
    private static final Pattern RE_TOKEN_SEARCH = Pattern.compile(":(\\w+)");

    /**
     * a
     */
    private final List<ScxMappingHandler> scxMappingHandlers;

    /**
     * 扫描所有被 ScxMapping注解标记的方法 并封装为 ScxMappingHandler.
     *
     * @param scx s
     */
    public ScxMappingRegistrar(Scx scx) {
        scxMappingHandlers = initScxMappingHandlers(scx);
    }

    /**
     * <p>initVertxRouteStateMethod.</p>
     *
     * @return a {@link java.lang.reflect.Method} object
     */
    private static Method initVertxRouteStateMethod() {
        try {
            var m = RouteImpl.class.getDeclaredMethod("state");
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此处因为 vertx 包访问权限端原因 我们无法直接获取 RouteState 这里通过反射进行特殊处理 然后使用一个 类似的数据结构进行包装
     *
     * @param r t
     * @return t
     */
    private static RouteState getRouteState(Route r) {
        try {
            return ObjectUtils.convertValue(vertxRouteStateMethod.invoke(r), RouteState.class);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 在注册路由前我们要进行一个排序 规则如下
     * <br>
     * 1 若注解上标识了 order 则按照注解上的 order 进行插入 如下
     * 0 > 5 > 13 > 199
     * <br>
     * 2 如果根据路径是否为精确路径 进行排序 如 如下
     * /api/user > /api/*
     * <br>
     * 3 根据路径参数数量进行排序 (按照参数数量倒序排序) 如下
     * /api/user/list > /api/user/:m > /api/:u/:m/
     *
     * @param scxMappingHandlers s
     * @return s
     */
    private static List<ScxMappingHandler> sortedScxMappingHandlers(List<ScxMappingHandler> scxMappingHandlers) {
        return scxMappingHandlers.stream()
                .sorted(orderComparator.thenComparing(exactPathComparator).thenComparing(groupsComparator))
                .toList();
    }

    /**
     * 填充 routeState
     *
     * @param list a
     * @param scx  a
     * @return a
     */
    private static List<ScxMappingHandler> fillRouteState(List<ScxMappingHandler> list, Scx scx) {
        var tempRouter = Router.router(scx.vertx());
        return list.stream().peek(c -> c.setRouteState(getRouteState(tempRouter.route(c.originalUrl)))).toList();
    }

    /**
     * a
     *
     * @param scx a
     * @return a
     */
    private static List<ScxMappingHandler> initScxMappingHandlers(Scx scx) {
        var scxMappingClassList = Arrays.stream(scx.scxModules())
                .flatMap(c -> c.classList().stream())
                .filter(ScxMappingRegistrar::isScxMappingClass)
                .toList();
        //获取所有的 handler
        var allScxMappingHandlers = scxMappingClassList.stream()
                .flatMap(c -> Arrays.stream(c.getMethods())
                        .filter(ScxMappingRegistrar::isScxMappingMethod)
                        .map(m -> new ScxMappingHandler(c, m, scx)))
                .toList();
        //填充 routeState
        var filledList = fillRouteState(allScxMappingHandlers, scx);
        //返回排序后的 handlers
        return sortedScxMappingHandlers(filledList);
    }

    /**
     * 初始化 ScxMappingClassList
     *
     * @param c a
     * @return a
     */
    public static boolean isScxMappingClass(Class<?> c) {
        return (c.isAnnotationPresent(ScxMapping.class) || c.isAnnotationPresent(Controller.class)) //拥有注解
                && ClassUtils.isNormalClass(c); // 是一个普通的类 (不是接口, 不是抽象类) ; 此处不要求有必须有无参构造函数 因为此类的创建会由 beanFactory 进行处理
    }

    /**
     * 判断是否为 ScxMapping 方法
     *
     * @param m a
     * @return a
     */
    public static boolean isScxMappingMethod(Method m) {
        return m.isAnnotationPresent(ScxMapping.class)  //拥有注解
                && !m.isBridge(); // 不是桥接方法
    }

    /**
     * 校验路由是否已经存在
     *
     * @param scxMappingHandlers a {@link java.util.List} object
     */
    private static void checkScxMappingHandlerRouteExists(List<ScxMappingHandler> scxMappingHandlers) {
        var m = new MultiMap<NormalPathInfo, ScxMappingHandler>();
        for (var scxMappingHandler : scxMappingHandlers) {
            var key = scxMappingHandler.routeState().pattern() != null
                    ? scxMappingHandler.routeState().pattern().toString()
                    : scxMappingHandler.routeState().path();
            for (var httpMethod : scxMappingHandler.httpMethods) {
                m.put(new NormalPathInfo(httpMethod, key), scxMappingHandler);
            }
        }
        var map = m.asMap();
        map.forEach((k, v) -> {
            if (v.size() > 1) { //具有多个路由
                var content = v.stream().map(c -> "\t" + c.clazz.getName() + " : " + c.method.getName()).collect(Collectors.joining(System.lineSeparator()));
                logger.error("检测到重复的路由!!! {} --> \"{}\" , 相关 class 及 方法 如下 ▼" + System.lineSeparator() + "{}",
                        k.httpMethod, getPatternUrl(v.get(0).originalUrl), content);
            }
        });
    }

    /**
     * 获取美化后的去除路径参数的 url 主要用来在判断重复路径中进行展示
     *
     * @param path p
     * @return r
     */
    private static String getPatternUrl(String path) {
        return RE_TOKEN_SEARCH.matcher(path).replaceAll("?");
    }

    /**
     * 获取所有被ScxMapping注解标记的方法的 handler
     *
     * @return 所有 handler
     */
    public List<ScxMappingHandler> scxMappingHandlers() {
        return new ArrayList<>(scxMappingHandlers);
    }

    /**
     * <p>registerRoute.</p>
     *
     * @param router a {@link io.vertx.ext.web.Router} object
     */
    public void registerRoute(Router router) {
        // 检查重复路由 (这里只需给出警告即可)
        checkScxMappingHandlerRouteExists(scxMappingHandlers);
        //循环添加到 vertxRouter 中
        for (var c : scxMappingHandlers) {
            var r = router.route(c.originalUrl);
            for (var httpMethod : c.httpMethods) {
                r.method(httpMethod.vertxMethod());
            }
            r.handler(c);
        }
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

    record NormalPathInfo(cool.scx.core.enumeration.HttpMethod httpMethod, String key) {

    }

}
