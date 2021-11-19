package cool.scx.mvc.processor;

import io.vertx.ext.web.RoutingContext;

/**
 * a
 */
public interface ScxMappingResultProcessor {

    /**
     * a
     *
     * @param result a
     * @return a
     */
    boolean canHandle(Object result);

    /**
     * 将结果处理并返回
     *
     * @param result  a
     * @param context a
     * @throws Exception a
     */
    void handle(Object result, RoutingContext context) throws Exception;

}
