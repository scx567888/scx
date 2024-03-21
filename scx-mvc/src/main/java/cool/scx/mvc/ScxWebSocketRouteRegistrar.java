package cool.scx.mvc;

import cool.scx.common.util.URIBuilder;
import cool.scx.common.util.reflect.ClassUtils;
import cool.scx.mvc.annotation.ScxWebSocketRoute;
import cool.scx.mvc.base.BaseWebSocketHandler;
import cool.scx.mvc.websocket.WebSocketRoute;
import cool.scx.mvc.websocket.WebSocketRouter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * <p>ScxWebSocketMappingRegistrar class.</p>
 *
 * @author scx567888
 * @version 1.18.0
 */
public final class ScxWebSocketRouteRegistrar {

    private static final Comparator<WebSocketRoute> orderComparator = Comparator.comparing(WebSocketRoute::order);

    private final List<WebSocketRoute> routes;

    public ScxWebSocketRouteRegistrar(Object... objects) {
        this.routes = initScxWebSocketRoutes(objects);
    }

    private static List<WebSocketRoute> initScxWebSocketRoutes(Object... objects) {
        var filteredObjectList = filterObject(objects);
        var routeList = filteredObjectList.stream().map(ScxWebSocketRouteRegistrar::createScxWebSocketRoute).toList();
        return sortedScxWebSocketRoutes(routeList);
    }

    public static WebSocketRoute createScxWebSocketRoute(BaseWebSocketHandler o) {
        var c = o.getClass();
        var scxWebSocketMapping = c.getAnnotation(ScxWebSocketRoute.class);
        var path = URIBuilder.addSlashStart(URIBuilder.join(scxWebSocketMapping.value()));
        var order = scxWebSocketMapping.order();
        return new WebSocketRoute(order, path, o);
    }

    public static List<BaseWebSocketHandler> filterObject(Object... classList) {
        return Arrays.stream(classList)
                .filter(o -> isScxWebSocketRouteClass(o.getClass()))
                .map(c -> (BaseWebSocketHandler) c)
                .toList();
    }

    @SuppressWarnings("unchecked")
    public static List<? extends Class<? extends BaseWebSocketHandler>> filterClass(List<Class<?>> classList) {
        return classList.stream()
                .filter(ScxWebSocketRouteRegistrar::isScxWebSocketRouteClass)
                .map(c -> (Class<? extends BaseWebSocketHandler>) c)
                .toList();
    }

    public static boolean isScxWebSocketRouteClass(Class<?> c) {
        return c.isAnnotationPresent(ScxWebSocketRoute.class) // 拥有注解
                && ClassUtils.isNormalClass(c) // 是一个普通的类 (不是接口, 不是抽象类) ; 此处不要求有必须有无参构造函数 因为此类的创建会由 beanFactory 进行处理
                && BaseWebSocketHandler.class.isAssignableFrom(c); // 继承自 BaseWebSocketHandler
    }

    private static List<WebSocketRoute> sortedScxWebSocketRoutes(List<WebSocketRoute> list) {
        return list.stream().sorted(orderComparator).toList();
    }

    public WebSocketRouter registerRoute(WebSocketRouter scxWebSocketRouter) {
        for (var route : routes) {
            scxWebSocketRouter.addRoute(route);
        }
        return scxWebSocketRouter;
    }

}
