package cool.scx.web.return_value_handler;

import cool.scx.http_server.ScxRoutingContext;

/**
 * 返回值处理器
 *
 * @author scx567888
 * @version 1.11.8
 */
public interface ReturnValueHandler {

    /**
     * 是否能够处理
     *
     * @param returnValue result
     * @return 是否能够处理
     */
    boolean canHandle(Object returnValue);

    /**
     * 处理结果
     *
     * @param returnValue    result
     * @param routingContext context
     * @throws java.lang.Exception e
     */
    void handle(Object returnValue, ScxRoutingContext routingContext) throws Exception;

}
