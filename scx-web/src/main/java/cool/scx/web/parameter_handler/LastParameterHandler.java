package cool.scx.web.parameter_handler;

import cool.scx.common.reflect.ParameterInfo;

import static cool.scx.common.util.ScxExceptionHelper.ignore;
import static cool.scx.web.parameter_handler.FromBodyParameterHandler.getValueFromBody;
import static cool.scx.web.parameter_handler.FromPathParameterHandler.getValueFromPath;
import static cool.scx.web.parameter_handler.FromQueryParameterHandler.getValueFromQuery;

/**
 * LastParameterHandler
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class LastParameterHandler implements ParameterHandler {

    @Override
    public boolean canHandle(ParameterInfo parameter) {
        return true;
    }

    @Override
    public Object handle(ParameterInfo parameter, RequestInfo requestInfo) throws Exception {
        var javaType = parameter.type();
        var name = parameter.name();
        //------ 这里针对没有注解的参数进行赋值猜测 ---------------
        //  从 body 里进行猜测 先尝试 根据参数名称进行转换
        Object value = ignore(() -> getValueFromBody(name, false, false, javaType, requestInfo));
        if (value == null) {
            // 再尝试将整体转换为 参数
            value = ignore(() -> getValueFromBody(null, true, false, javaType, requestInfo));
            if (value == null) {
                //从查询参数里进行猜测
                value = ignore(() -> getValueFromQuery(name, false, false, javaType, requestInfo));
                if (value == null) {
                    //从路径进行猜测
                    value = ignore(() -> getValueFromPath(name, false, false, javaType, requestInfo));
                }
            }
        }
        return value;
    }

}
