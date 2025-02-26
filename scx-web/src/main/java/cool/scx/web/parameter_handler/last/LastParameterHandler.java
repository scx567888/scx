package cool.scx.web.parameter_handler.last;

import cool.scx.reflect.IParameterInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.RequestInfo;

import static cool.scx.common.exception.ScxExceptionHelper.ignore;
import static cool.scx.web.parameter_handler.from_body.FromBodyParameterHandler.getValueFromBody;
import static cool.scx.web.parameter_handler.from_path.FromPathParameterHandler.getValueFromPath;
import static cool.scx.web.parameter_handler.from_query.FromQueryParameterHandler.getValueFromQuery;

/**
 * LastParameterHandler
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class LastParameterHandler implements ParameterHandler {

    private final IParameterInfo parameter;

    public LastParameterHandler(IParameterInfo parameter) {
        this.parameter = parameter;
    }

    @Override
    public Object handle(RequestInfo requestInfo) throws Exception {
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
