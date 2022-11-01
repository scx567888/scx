package cool.scx.core.websocket;

import cool.scx.core.Scx;
import cool.scx.core.annotation.ScxWebSocketMapping;
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
    private static List<ScxWebSocketRoute> initScxWebSocketRoutes(Scx scx) {
        //获取所有的 handler
        var list = Arrays.stream(scx.scxModules())
                .flatMap(c -> c.scxWebSocketRouteClassList().stream())
                .map(c -> {
                    var scxWebSocketMapping = c.getAnnotation(ScxWebSocketMapping.class);
                    var path = URIBuilder.join(scxWebSocketMapping.value());
                    var order = scxWebSocketMapping.order();
                    var baseWebSocketHandler = scx.scxBeanFactory().getBean(c);
                    return new ScxWebSocketRoute(order, path, baseWebSocketHandler);
                })
                .toList();
        return sortedScxWebSocketRoutes(list);
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
