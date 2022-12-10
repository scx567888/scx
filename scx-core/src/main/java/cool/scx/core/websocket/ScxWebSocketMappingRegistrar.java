package cool.scx.core.websocket;

import cool.scx.core.Scx;
import cool.scx.core.annotation.ScxWebSocketMapping;
import cool.scx.core.base.BaseWebSocketHandler;
import cool.scx.util.ClassUtils;
import cool.scx.util.URIBuilder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * <p>ScxWebSocketMappingRegistrar class.</p>
 *
 * @author scx567888
 * @version 1.18.0
 */
public class ScxWebSocketMappingRegistrar {

    /**
     * Constant <code>orderComparator</code>
     */
    private static final Comparator<ScxWebSocketRoute> orderComparator = Comparator.comparing(ScxWebSocketRoute::order);

    private final List<ScxWebSocketRoute> scxWebSocketRoutes;

    /**
     * <p>Constructor for ScxWebSocketMappingRegistrar.</p>
     *
     * @param scx a {@link cool.scx.core.Scx} object
     */
    public ScxWebSocketMappingRegistrar(Scx scx) {
        this.scxWebSocketRoutes = initScxWebSocketRoutes(scx);
    }

    /**
     * <p>initScxWebSocketRoutes.</p>
     *
     * @param scx a {@link cool.scx.core.Scx} object
     * @return a {@link java.util.List} object
     */
    @SuppressWarnings("unchecked")
    private static List<ScxWebSocketRoute> initScxWebSocketRoutes(Scx scx) {
        var scxWebSocketRouteClassList = Arrays.stream(scx.scxModules())
                .flatMap(c -> c.classList().stream())
                .filter(ScxWebSocketMappingRegistrar::isScxWebSocketRouteClass)
                .map(c -> (Class<? extends BaseWebSocketHandler>) c)
                .toList();
        //获取所有的 handler
        var list = scxWebSocketRouteClassList.stream()
                .map(c -> {
                    var scxWebSocketMapping = c.getAnnotation(ScxWebSocketMapping.class);
                    var path = URIBuilder.addSlashStart(URIBuilder.join(scxWebSocketMapping.value()));
                    var order = scxWebSocketMapping.order();
                    var baseWebSocketHandler = scx.scxBeanFactory().getBean(c);
                    return new ScxWebSocketRoute(order, path, baseWebSocketHandler);
                })
                .toList();
        return sortedScxWebSocketRoutes(list);
    }

    /**
     * <p>isScxWebSocketRouteClass.</p>
     *
     * @param c a {@link java.lang.Class} object
     * @return a boolean
     */
    public static boolean isScxWebSocketRouteClass(Class<?> c) {
        return c.isAnnotationPresent(ScxWebSocketMapping.class) // 拥有注解
                && ClassUtils.isNormalClass(c) // 是一个普通的类 (不是接口, 不是抽象类) ; 此处不要求有必须有无参构造函数 因为此类的创建会由 beanFactory 进行处理
                && BaseWebSocketHandler.class.isAssignableFrom(c); // 继承自 BaseWebSocketHandler
    }

    /**
     * <p>sortedScxWebSocketRoutes.</p>
     *
     * @param list a {@link java.util.List} object
     * @return a {@link java.util.List} object
     */
    private static List<ScxWebSocketRoute> sortedScxWebSocketRoutes(List<ScxWebSocketRoute> list) {
        return list.stream().sorted(orderComparator).toList();
    }

    /**
     * <p>registerRoute.</p>
     *
     * @param scxWebSocketRouter a {@link cool.scx.core.websocket.ScxWebSocketRouter} object
     */
    public void registerRoute(ScxWebSocketRouter scxWebSocketRouter) {
        for (var route : scxWebSocketRoutes) {
            scxWebSocketRouter.addRoute(route);
        }
    }

}
