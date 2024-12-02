package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

/**
 * ParameterInfoHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
class ParameterInfoHelper {


    //************************* ParameterInfo START **************

    static String _findName(ParameterInfo parameterInfo) {
        return parameterInfo.parameter().getName();
    }

    static JavaType _findType(ParameterInfo parameterInfo) {
        return ReflectHelper._findType(parameterInfo.parameter().getParameterizedType(), parameterInfo.executableInfo().classInfo());
    }

    //************************* ParameterInfo END **************
}
