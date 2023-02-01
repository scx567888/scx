package cool.scx.mvc;

import cool.scx.mvc.annotation.ScxWebSocketMapping;
import cool.scx.mvc.base.BaseWebSocketHandler;
import cool.scx.mvc.websocket.ScxWebSocketRoute;
import cool.scx.mvc.websocket.ScxWebSocketRouter;
import cool.scx.util.ClassUtils;
import cool.scx.util.URIBuilder;
import org.springframework.beans.factory.BeanFactory;

import java.util.Comparator;
import java.util.List;

/**
 * <p>ScxWebSocketMappingRegistrar class.</p>
 *
 * @author scx567888
 * @version 1.18.0
 */
public class ScxWebSocketMappingRegistrar {

    private static final Comparator<ScxWebSocketRoute> orderComparator = Comparator.comparing(ScxWebSocketRoute::order);

    private final List<ScxWebSocketRoute> scxWebSocketRoutes;

    public ScxWebSocketMappingRegistrar(BeanFactory beanFactory, List<Class<?>> classList) {
        this.scxWebSocketRoutes = initScxWebSocketRoutes(beanFactory, classList);
    }

    @SuppressWarnings("unchecked")
    private static List<ScxWebSocketRoute> initScxWebSocketRoutes(BeanFactory beanFactory, List<Class<?>> classList) {
        var scxWebSocketRouteClassList = classList.stream()
                .filter(ScxWebSocketMappingRegistrar::isScxWebSocketRouteClass)
                .map(c -> (Class<? extends BaseWebSocketHandler>) c)
                .toList();
        //获取所有的 handler
        var list = scxWebSocketRouteClassList.stream()
                .map(c -> {
                    var scxWebSocketMapping = c.getAnnotation(ScxWebSocketMapping.class);
                    var path = URIBuilder.addSlashStart(URIBuilder.join(scxWebSocketMapping.value()));
                    var order = scxWebSocketMapping.order();
                    var baseWebSocketHandler = beanFactory.getBean(c);
                    return new ScxWebSocketRoute(order, path, baseWebSocketHandler);
                })
                .toList();
        return sortedScxWebSocketRoutes(list);
    }

    public static boolean isScxWebSocketRouteClass(Class<?> c) {
        return c.isAnnotationPresent(ScxWebSocketMapping.class) // 拥有注解
                && ClassUtils.isNormalClass(c) // 是一个普通的类 (不是接口, 不是抽象类) ; 此处不要求有必须有无参构造函数 因为此类的创建会由 beanFactory 进行处理
                && BaseWebSocketHandler.class.isAssignableFrom(c); // 继承自 BaseWebSocketHandler
    }

    private static List<ScxWebSocketRoute> sortedScxWebSocketRoutes(List<ScxWebSocketRoute> list) {
        return list.stream().sorted(orderComparator).toList();
    }

    public ScxWebSocketRouter registerRoute(ScxWebSocketRouter scxWebSocketRouter) {
        for (var route : scxWebSocketRoutes) {
            scxWebSocketRouter.addRoute(route);
        }
        return scxWebSocketRouter;
    }

}
