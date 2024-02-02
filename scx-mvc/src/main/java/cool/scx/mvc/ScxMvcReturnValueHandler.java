package cool.scx.mvc;

import io.vertx.ext.web.RoutingContext;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public interface ScxMvcReturnValueHandler {

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
     * @throws java.lang.Exception a
     */
    void handle(Object result, RoutingContext context) throws Exception;

}
