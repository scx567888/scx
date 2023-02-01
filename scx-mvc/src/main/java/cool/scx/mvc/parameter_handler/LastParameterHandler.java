package cool.scx.mvc.parameter_handler;

import cool.scx.mvc.ScxMvcParameterHandler;
import cool.scx.mvc.ScxMvcRequestInfo;
import cool.scx.util.ObjectUtils;

import java.lang.reflect.Parameter;

import static cool.scx.mvc.parameter_handler.FromBodyParameterHandler.getValueFromBody;
import static cool.scx.mvc.parameter_handler.FromPathParameterHandler.getValueFromPath;
import static cool.scx.mvc.parameter_handler.FromQueryParameterHandler.getValueFromQuery;
import static cool.scx.util.ScxExceptionHelper.ignore;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class LastParameterHandler implements ScxMvcParameterHandler {

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
    public Object handle(Parameter parameter, ScxMvcRequestInfo context) throws Exception {
        var javaType = ObjectUtils.constructType(parameter.getParameterizedType());
        var name = parameter.getName();
        //------ 这里针对没有注解的参数进行赋值猜测 ---------------
        //  从 body 里进行猜测 先尝试 根据参数名称进行转换
        Object value = ignore(() -> getValueFromBody(name, false, false, javaType, context));
        if (value == null) {
            // 再尝试将整体转换为 参数
            value = ignore(() -> getValueFromBody(null, true, false, javaType, context));
            if (value == null) {
                //从查询参数里进行猜测
                value = ignore(() -> getValueFromQuery(name, false, false, javaType, context));
                if (value == null) {
                    //从路径进行猜测
                    value = ignore(() -> getValueFromPath(name, false, false, javaType, context));
                }
            }
        }
        return value;
    }

}
