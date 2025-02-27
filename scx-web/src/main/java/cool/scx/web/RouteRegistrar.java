package cool.scx.web;

import cool.scx.common.multi_map.MultiMap;
import cool.scx.common.util.ClassUtils;
import cool.scx.http.routing.PathMatcherImpl;
import cool.scx.http.routing.Router;
import cool.scx.reflect.ClassInfoFactory;
import cool.scx.reflect.MethodInfo;
import cool.scx.web.annotation.NoScxRoute;
import cool.scx.web.annotation.ScxRoute;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cool.scx.reflect.AccessModifier.PUBLIC;
import static java.lang.System.Logger.Level.WARNING;

/**
 * 路由注册器
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class RouteRegistrar {

    private static final Logger logger = System.getLogger(RouteRegistrar.class.getName());
    private static final Comparator<ScxRouteHandler> EXACT_PATH_COMPARATOR = Comparator.comparing(r -> {
        var p = (PathMatcherImpl) r.pathMatcher();
        return p.exactPath() ? 0 : 1;
    });
    private static final Comparator<ScxRouteHandler> GROUPS_COMPARATOR = Comparator.comparing(r -> {
        var p = (PathMatcherImpl) r.pathMatcher();
        return p.groups() == null ? 0 : p.groups().size();
    });
    private static final Comparator<ScxRouteHandler> ORDER_COMPARATOR = Comparator.comparing(ScxRouteHandler::order);
    private static final Pattern RE_TOKEN_SEARCH = Pattern.compile(":(\\w+)");
    private final ScxWeb scxWeb;

    /**
     * 扫描所有被 ScxMapping注解标记的方法 并封装为 ScxMappingHandler.
     */
    public RouteRegistrar(ScxWeb scxWeb) {
        this.scxWeb = scxWeb;
    }

    private static List<ScxRouteHandler> initScxRouteHandlers(ScxWeb scxWeb, Object... objects) {
        var filteredObjectList = filterObject(objects);
        var handlers = new ArrayList<ScxRouteHandler>();
        for (var c : filteredObjectList) {
            var methods = filterMethod(c);
            for (var m : methods) {
                handlers.add(createScxRouteHandler(m, c, scxWeb));
            }
        }
        return sortedScxRouteHandlers(handlers);
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
        return handlers.stream().sorted(ORDER_COMPARATOR.thenComparing(EXACT_PATH_COMPARATOR).thenComparing(GROUPS_COMPARATOR)).toList();
    }

    private static ScxRouteHandler createScxRouteHandler(MethodInfo m, Object bean, ScxWeb scxWeb) {
        return new ScxRouteHandler(m, bean, scxWeb);
    }

    private static List<Object> filterObject(Object... objects) {
        return Arrays.stream(objects).filter(o -> isRoute(o.getClass())).toList();
    }

    public static List<Class<?>> filterClass(List<Class<?>> classList) {
        return classList.stream().filter(RouteRegistrar::isRoute).toList();
    }

    private static List<MethodInfo> filterMethod(Object object) {
        return Arrays.stream(ClassInfoFactory.getClassInfo(object.getClass()).allMethods()).filter(m -> m.accessModifier() == PUBLIC && isRoute(m)).toList();
    }

    /**
     * 初始化 ScxMappingClassList
     *
     * @param c a
     * @return a
     */
    public static boolean isRoute(Class<?> c) {
        return c.isAnnotationPresent(ScxRoute.class) && //拥有注解
                ClassUtils.isNormalClass(c); // 是一个普通的类 (不是接口, 不是抽象类) ; 此处不要求有必须有无参构造函数 因为此类的创建会由 beanFactory 进行处理
    }

    /**
     * 判断是否为 ScxMapping 方法
     *
     * @param m a
     * @return a
     */
    public static boolean isRoute(MethodInfo m) {
        var noScxRoute = m.findAnnotation(NoScxRoute.class);
        if (noScxRoute != null) {
            return false;
        }
        var s = findScxRoute(m);
        return s != null;
    }

    public static ScxRoute findScxRoute(MethodInfo method) {
        var annotations = method.allAnnotations();
        for (var a : annotations) {
            if (a instanceof ScxRoute s) {
                return s;
            }
        }
        return null;
    }

    public static ScxRoute findScxRouteOrThrow(MethodInfo method) {
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
            var p = (PathMatcherImpl) handler.pathMatcher();
            var key = p.pattern() != null ? p.pattern().toString() : p.path();
            if (handler.methods().isEmpty()) {
                m.add(new NormalPathInfo("*", key), handler);
            } else {
                for (var httpMethod : handler.methods()) {
                    m.add(new NormalPathInfo(httpMethod.name(), key), handler);
                }
            }
        }
        var map = m.toMultiValueMap();
        map.forEach((k, v) -> {
            if (v.size() > 1) { //具有多个路由
                var content = v.stream().map(c -> "\t" + c.clazz.getName() + " : " + c.method.name()).collect(Collectors.joining(System.lineSeparator()));
                logger.log(WARNING, "检测到重复的路由!!! {0} --> \"{1}\" , 相关 class 及 方法 如下 ▼" + System.lineSeparator() + "{2}",
                        k.httpMethod(), getPatternUrl(v.get(0).path()), content);
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

    public Router registerRoute(Router router, Object... objects) {
        var scxRouteHandlers = initScxRouteHandlers(this.scxWeb, objects);
        // 检查重复路由 (这里只需给出警告即可)
        checkRouteExists(scxRouteHandlers);
        //循环添加到 router 中
        for (var c : scxRouteHandlers) {
            router.addRoute(c);
        }
        return router;
    }

    private record NormalPathInfo(String httpMethod, String path) {

    }

}
