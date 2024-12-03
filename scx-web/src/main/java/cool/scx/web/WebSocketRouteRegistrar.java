package cool.scx.web;

import cool.scx.common.util.ClassUtils;
import cool.scx.common.util.URIUtils;
import cool.scx.http.routing.WebSocketRoute;
import cool.scx.http.routing.WebSocketRouter;
import cool.scx.web.annotation.ScxWebSocketRoute;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * WebSocketRouteRegistrar
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class WebSocketRouteRegistrar {

    private static final Comparator<WebSocketRoute> orderComparator = Comparator.comparing(WebSocketRoute::order);
    private final ScxWeb scxWeb;

    public WebSocketRouteRegistrar(ScxWeb scxWeb) {
        this.scxWeb = scxWeb;
    }

    private static List<WebSocketRoute> initWebSocketRoutes(Object... objects) {
        var filteredObjectList = filterObject(objects);
        var routeList = filteredObjectList.stream().map(WebSocketRouteRegistrar::createWebSocketRoute).toList();
        return sortedWebSocketRoutes(routeList);
    }

    public static WebSocketRoute createWebSocketRoute(BaseWebSocketHandler o) {
        var c = o.getClass();
        var scxWebSocketMapping = c.getAnnotation(ScxWebSocketRoute.class);
        var path = URIUtils.addSlashStart(URIUtils.join(scxWebSocketMapping.value()));
        var order = scxWebSocketMapping.order();
        //todo 需要重新设计
        return WebSocketRoute.of().order(order).path(path).handler((d) -> {
            try {
                o.onOpen(d);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static List<BaseWebSocketHandler> filterObject(Object... classList) {
        return Arrays.stream(classList)
                .filter(o -> isWebSocketRouteClass(o.getClass()))
                .map(c -> (BaseWebSocketHandler) c)
                .toList();
    }

    @SuppressWarnings("unchecked")
    public static List<? extends Class<? extends BaseWebSocketHandler>> filterClass(List<Class<?>> classList) {
        return classList.stream()
                .filter(WebSocketRouteRegistrar::isWebSocketRouteClass)
                .map(c -> (Class<? extends BaseWebSocketHandler>) c)
                .toList();
    }

    public static boolean isWebSocketRouteClass(Class<?> c) {
        return c.isAnnotationPresent(ScxWebSocketRoute.class) && // 拥有注解
                ClassUtils.isNormalClass(c) && // 是一个普通的类 (不是接口, 不是抽象类) ; 此处不要求有必须有无参构造函数 因为此类的创建会由 beanFactory 进行处理
                BaseWebSocketHandler.class.isAssignableFrom(c); // 继承自 BaseWebSocketHandler
    }

    private static List<WebSocketRoute> sortedWebSocketRoutes(List<WebSocketRoute> list) {
        return list.stream().sorted(orderComparator).toList();
    }

    public WebSocketRouter registerRoute(WebSocketRouter scxWebSocketRouter, Object... objects) {
        var routes = initWebSocketRoutes(objects);
        for (var route : routes) {
            scxWebSocketRouter.addRoute(route);
        }
        return scxWebSocketRouter;
    }

}
