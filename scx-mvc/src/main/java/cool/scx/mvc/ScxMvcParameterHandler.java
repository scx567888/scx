package cool.scx.mvc;

import java.lang.reflect.Parameter;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public interface ScxMvcParameterHandler {

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
     * @param parameter 方法参数
     * @param info      包装后的 RoutingContext
     * @return 处理后的结果
     * @throws java.lang.Exception e
     */
    Object handle(Parameter parameter, ScxMappingRoutingContextInfo info) throws Exception;

}
