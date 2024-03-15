package cool.scx.mvc;

import cool.scx.mvc.annotation.NoScxRoute;
import cool.scx.mvc.annotation.ScxRoute;
import cool.scx.common.util.MultiMap;
import cool.scx.common.util.ObjectUtils;
import cool.scx.common.util.reflect.ClassUtils;
import cool.scx.common.util.reflect.MethodUtils;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.impl.RouteImpl;
import org.springframework.stereotype.Controller;

import java.lang.System.Logger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.System.Logger.Level.WARNING;

/**
 * 路由管理器
 *
 * @author scx567888
 * @version 1.17.8
 */
public final class ScxRouteRegistrar {

    private static final Logger logger = System.getLogger(ScxRouteRegistrar.class.getName());

    private static final Method vertxRouteStateMethod = initVertxRouteStateMethod();

    private static final Comparator<ScxRouteHandler> exactPathComparator = Comparator.comparing(routeState -> routeState.routeState().getExactPathOrder());

    private static final Comparator<ScxRouteHandler> groupsComparator = Comparator.comparing(routeState -> routeState.routeState().getGroupsOrder());

    private static final Comparator<ScxRouteHandler> orderComparator = Comparator.comparing(ScxRouteHandler::order);

    private static final Pattern RE_TOKEN_SEARCH = Pattern.compile(":(\\w+)");

    private final List<ScxRouteHandler> scxRouteHandlers;

    /**
     * 扫描所有被 ScxMapping注解标记的方法 并封装为 ScxMappingHandler.
     */
    public ScxRouteRegistrar(ScxMvc scxMvc, Object... objects) {
        this.scxRouteHandlers = initScxRouteHandlers(scxMvc, objects);
    }

    private static List<ScxRouteHandler> initScxRouteHandlers(ScxMvc scxMvc, Object... objects) {
        var filteredObjectList = filterObject(objects);
        var handlers = new ArrayList<ScxRouteHandler>();
        for (var c : filteredObjectList) {
            var methods = filterMethod(c);
            for (var m : methods) {
                handlers.add(createScxRouteHandler(m, c, scxMvc));
            }
        }
        return sortedScxRouteHandlers(handlers);
    }

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
    private static ScxRouteHandler.RouteState getRouteState(Route r) {
        try {
            return ObjectUtils.convertValue(vertxRouteStateMethod.invoke(r), ScxRouteHandler.RouteState.class);
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
     * @param handlers s
     * @return s
     */
    private static List<ScxRouteHandler> sortedScxRouteHandlers(List<ScxRouteHandler> handlers) {
        var list = fillRouteState(handlers);
        return list.stream().sorted(orderComparator.thenComparing(exactPathComparator).thenComparing(groupsComparator)).toList();
    }

    /**
     * 填充 routeState
     *
     * @param list a
     * @return a
     */
    private static List<ScxRouteHandler> fillRouteState(List<ScxRouteHandler> list) {
        var tempRouter = Router.router(null);
        return list.stream().peek(c -> c.setRouteState(getRouteState(tempRouter.route(c.originalUrl)))).toList();
    }

    private static ScxRouteHandler createScxRouteHandler(Method m, Object bean, ScxMvc scxMvc) {
        return new ScxRouteHandler(m, bean, scxMvc);
    }

    private static List<Object> filterObject(Object... objects) {
        return Arrays.stream(objects).filter(o -> isRoute(o.getClass())).toList();
    }

    public static List<Class<?>> filterClass(List<Class<?>> classList) {
        return classList.stream().filter(ScxRouteRegistrar::isRoute).toList();
    }

    private static List<Method> filterMethod(Object object) {
        return Arrays.stream(MethodUtils.findMethods(object.getClass())).filter(ScxRouteRegistrar::isRoute).toList();
    }

    /**
     * 初始化 ScxMappingClassList
     *
     * @param c a
     * @return a
     */
    public static boolean isRoute(Class<?> c) {
        return (c.isAnnotationPresent(ScxRoute.class) || c.isAnnotationPresent(Controller.class)) //拥有注解
               && ClassUtils.isNormalClass(c); // 是一个普通的类 (不是接口, 不是抽象类) ; 此处不要求有必须有无参构造函数 因为此类的创建会由 beanFactory 进行处理
    }

    /**
     * 判断是否为 ScxMapping 方法
     *
     * @param m a
     * @return a
     */
    public static boolean isRoute(Method m) {
        if (m.isBridge()) {
            return false; // 不是桥接方法
        }
        var noScxRoute = m.getAnnotation(NoScxRoute.class);
        if (noScxRoute != null) {
            return false;
        }
        var s = findScxRoute(m);
        return s != null;
    }

    public static ScxRoute findScxRoute(Method method) {
        var annotations = MethodUtils.findAllAnnotations(method);
        for (var a : annotations) {
            if (a instanceof ScxRoute s) {
                return s;
            }
        }
        return null;
    }

    public static ScxRoute findScxRouteOrThrow(Method method) {
        var scxRoute = findScxRoute(method);
        if (scxRoute == null) {
            throw new NullPointerException();
        }
        return scxRoute;
    }

    /**
     * 校验路由是否已经存在
     *
     * @param handlers a {@link java.util.List} object
     */
    private static void checkRouteExists(List<ScxRouteHandler> handlers) {
        var m = new MultiMap<NormalPathInfo, ScxRouteHandler>();
        for (var handler : handlers) {
            var key = handler.routeState().pattern() != null
                    ? handler.routeState().pattern().toString()
                    : handler.routeState().path();
            if (handler.httpMethods.length == 0) {
                m.put(new NormalPathInfo("*", key), handler);
            } else {
                for (var httpMethod : handler.httpMethods) {
                    m.put(new NormalPathInfo(httpMethod.name(), key), handler);
                }
            }
        }
        var map = m.toMultiValueMap();
        map.forEach((k, v) -> {
            if (v.size() > 1) { //具有多个路由
                var content = v.stream().map(c -> "\t" + c.clazz.getName() + " : " + c.method.getName()).collect(Collectors.joining(System.lineSeparator()));
                logger.log(WARNING, "检测到重复的路由!!! {0} --> \"{1}\" , 相关 class 及 方法 如下 ▼" + System.lineSeparator() + "{2}",
                        k.httpMethod(), getPatternUrl(v.get(0).originalUrl), content);
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
    public List<ScxRouteHandler> scxRouteHandlers() {
        return new ArrayList<>(scxRouteHandlers);
    }

    /**
     * <p>registerRoute.</p>
     *
     * @param router a {@link io.vertx.ext.web.Router} object
     * @return a
     */
    public Router registerRoute(Router router) {
        // 检查重复路由 (这里只需给出警告即可)
        checkRouteExists(scxRouteHandlers);
        //循环添加到 vertxRouter 中
        for (var c : scxRouteHandlers) {
            var r = router.route(c.originalUrl);
            for (var httpMethod : c.httpMethods) {
                r.method(httpMethod.vertxMethod());
            }
            for (var consumes : c.consumes) {
                r.consumes(consumes);
            }
            for (var produces : c.produces) {
                r.produces(produces);
            }
            r.handler(c);
        }
        return router;
    }

    private record NormalPathInfo(String httpMethod, String key) {

    }

}
