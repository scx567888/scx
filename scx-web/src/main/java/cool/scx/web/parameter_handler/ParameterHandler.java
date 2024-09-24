package cool.scx.web.parameter_handler;

import cool.scx.reflect.ParameterInfo;

/**
 * 参数处理器
 *
 * @author scx567888
 * @version 1.11.8
 */
public interface ParameterHandler {

    /**
     * 判断是否可以处理这个参数类型
     *
     * @param parameter 参数实例
     * @return 是否能够处理
     */
    boolean canHandle(ParameterInfo parameter);

    /**
     * 将结果处理并返回
     *
     * @param parameter   方法参数
     * @param requestInfo 包装后的 RoutingContext
     * @return 处理后的结果
     * @throws java.lang.Exception e
     */
    Object handle(ParameterInfo parameter, RequestInfo requestInfo) throws Exception;

}
