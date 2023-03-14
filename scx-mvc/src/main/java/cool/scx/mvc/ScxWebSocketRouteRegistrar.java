package cool.scx.mvc;

import cool.scx.mvc.annotation.ScxWebSocketRoute;
import cool.scx.mvc.base.BaseWebSocketHandler;
import cool.scx.mvc.websocket.WebSocketRoute;
import cool.scx.mvc.websocket.WebSocketRouter;
import cool.scx.util.URIBuilder;
import cool.scx.util.reflect.ClassUtils;
import org.springframework.beans.factory.BeanFactory;

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

    private final List<WebSocketRoute> scxWebSocketRoutes;

    public ScxWebSocketRouteRegistrar(BeanFactory beanFactory, List<Class<?>> classList) {
        this.scxWebSocketRoutes = initScxWebSocketRoutes(beanFactory, classList);
    }

    private static List<WebSocketRoute> initScxWebSocketRoutes(BeanFactory beanFactory, List<Class<?>> classList) {
        var filteredClassList = filterClass(classList);
        var routeList = filteredClassList.stream().map(c -> createScxWebSocketRoute(beanFactory, c)).toList();
        return sortedScxWebSocketRoutes(routeList);
    }

    public static WebSocketRoute createScxWebSocketRoute(BeanFactory beanFactory, Class<? extends BaseWebSocketHandler> c) {
        var scxWebSocketMapping = c.getAnnotation(ScxWebSocketRoute.class);
        var path = URIBuilder.addSlashStart(URIBuilder.join(scxWebSocketMapping.value()));
        var order = scxWebSocketMapping.order();
        var baseWebSocketHandler = beanFactory.getBean(c);
        return new WebSocketRoute(order, path, baseWebSocketHandler);
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
        for (var route : scxWebSocketRoutes) {
            scxWebSocketRouter.addRoute(route);
        }
        return scxWebSocketRouter;
    }

}
