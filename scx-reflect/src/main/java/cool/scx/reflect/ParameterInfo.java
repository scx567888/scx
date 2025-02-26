package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.reflect.Parameter;

import static cool.scx.reflect.ClassInfoHelper._findType;

/// ParameterInfo
///
/// @author scx567888
/// @version 0.0.1
public final class ParameterInfo {

    private final Parameter parameter;
    private final ExecutableInfo executableInfo;
    private final String name;
    private final JavaType type;

    ParameterInfo(Parameter parameter, ExecutableInfo executableInfo) {
        this.parameter = parameter;
        this.executableInfo = executableInfo;
        this.name = this.parameter.getName();
        this.type = _findType(parameter.getParameterizedType(), executableInfo.classInfo());
    }

    public Parameter parameter() {
        return parameter;
    }

    public ExecutableInfo executableInfo() {
        return executableInfo;
    }

    public String name() {
        return name;
    }

    public JavaType type() {
        return type;
    }

}
