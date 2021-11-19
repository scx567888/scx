package cool.scx.web;

import cool.scx.ScxBeanFactory;
import cool.scx.ScxModule;
import cool.scx.ScxModuleInfo;
import cool.scx.annotation.ScxWebSocketMapping;
import cool.scx.util.StringUtils;

import java.util.List;

/**
 * a
 */
public final class ScxWebSocketRouteRegistry {

    /**
     * a
     *
     * @param scxWebSocketRouter a
     * @param scxModuleInfos     a
     * @param scxBeanFactory     a
     */
    public static void registerAllRoute(ScxWebSocketRouter scxWebSocketRouter, List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos, ScxBeanFactory scxBeanFactory) {
        for (var scxModuleInfo : scxModuleInfos) {
            for (var c : scxModuleInfo.scxWebSocketRouteClassList()) {
                var annotation = c.getAnnotation(ScxWebSocketMapping.class);
                if (annotation != null) {
                    scxWebSocketRouter.addRoute(new ScxWebSocketRoute()
                            .path(StringUtils.cleanHttpURL(annotation.value()))
                            .baseWebSocketHandler(scxBeanFactory.getBean(c)));
                }
            }
        }
    }

}
