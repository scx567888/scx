package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.reflect.Parameter;

/// ParameterInfoHelper
///
/// @author scx567888
/// @version 0.0.1
class ParameterInfoHelper {

    static String _findName(Parameter parameter) {
        return parameter.getName();
    }

    static JavaType _findType(ParameterInfo parameterInfo) {
        return ReflectHelper._findType(parameterInfo.parameter().getParameterizedType(), parameterInfo.executableInfo().classInfo());
    }

}
