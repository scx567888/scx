package cool.scx.core.mvc.parameter_handler;

import cool.scx.core.mvc.ScxMappingMethodParameterHandler;
import cool.scx.core.mvc.ScxMappingRoutingContextInfo;

import java.lang.reflect.Parameter;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class LastMethodParameterHandler implements ScxMappingMethodParameterHandler {

    /**
     * a
     */
    public static final LastMethodParameterHandler DEFAULT_INSTANCE = new LastMethodParameterHandler();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Parameter parameter) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Parameter parameter, ScxMappingRoutingContextInfo context) throws Exception {
        //------ 这里针对没有注解的参数进行赋值猜测 ---------------
        //  从 body 里进行猜测 先尝试 根据参数名称进行转换
        Object value = FromBodyMethodParameterHandler.getValueFromBody(parameter.getName(), false, false, parameter.getParameterizedType(), context);
        if (value == null) {
            // 再尝试将整体转换为 参数
            value = FromBodyMethodParameterHandler.getValueFromBody(null, true, false, parameter.getParameterizedType(), context);
            if (value == null) {
                //从查询参数里进行猜测
                value = FromQueryMethodParameterHandler.getValueFromQuery(parameter.getName(), false, false, parameter.getParameterizedType(), context);
                if (value == null) {
                    //从路径进行猜测
                    value = FromPathMethodParameterHandler.getValueFromPath(parameter.getName(), false, false, parameter.getParameterizedType(), context);
                }
            }
        }
        return value;
    }

}
