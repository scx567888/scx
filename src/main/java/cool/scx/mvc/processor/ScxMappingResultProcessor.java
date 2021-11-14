package cool.scx.mvc.processor;

import io.vertx.ext.web.RoutingContext;

public interface ScxMappingResultProcessor {

    boolean canHandle(Object result);

    /**
     * 将结果处理并返回
     */
    void handle(Object result, RoutingContext context) throws Exception;

}
