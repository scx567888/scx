package cool.scx.web.exception_handler;

import cool.scx.ScxContext;
import cool.scx.enumeration.ScxFeature;
import cool.scx.exception.ScxHttpException;
import cool.scx.util.ExceptionUtils;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public abstract class ScxRouterExceptionHandler implements Handler<RoutingContext> {

    /**
     * a
     */
    private String info = "";

    public String info() {
        return info;
    }

    public void info(String info) {
        this.info = info;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        var t = routingContext.failure();
        if (t instanceof ScxHttpException scxHttpException) {
            scxHttpException.sendException(routingContext);
        } else {
            getScxHttpException(t != null && ScxContext.getFeatureState(ScxFeature.USE_DEVELOPMENT_ERROR_PAGE)
                    ? ExceptionUtils.getCustomStackTrace(t)
                    : info()).sendException(routingContext);
        }
    }

    public abstract ScxHttpException getScxHttpException(String info);

}
