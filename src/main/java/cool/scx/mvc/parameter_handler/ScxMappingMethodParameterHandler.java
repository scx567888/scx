package cool.scx.mvc.parameter_handler;

import java.lang.reflect.Parameter;

public interface ScxMappingMethodParameterHandler {

    /**
     * 判断是否可以处理这个参数类型
     *
     * @param parameter 参数实例
     * @return a
     */
    boolean canHandle(Parameter parameter);

    /**
     * 将结果处理并返回
     *
     * @param parameter      a
     * @param routingContext a
     */
    Object handle(Parameter parameter, ScxMappingRoutingContextInfo scxMappingRoutingContextInfo) throws Exception;

}
