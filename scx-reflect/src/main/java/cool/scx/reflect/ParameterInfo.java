package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.reflect.Parameter;

import static cool.scx.reflect.ParameterInfoHelper._findName;
import static cool.scx.reflect.ParameterInfoHelper._findType;

/// ParameterInfo
///
/// @author scx567888
/// @version 0.0.1
public final class ParameterInfo implements IParameterInfo {

    private final Parameter parameter;
    private final IExecutableInfo executableInfo;
    private final String name;
    private final JavaType type;

    ParameterInfo(Parameter parameter, IExecutableInfo executableInfo) {
        this.parameter = parameter;
        this.executableInfo = executableInfo;
        this.name = _findName(parameter);
        this.type = _findType(this);
    }

    @Override
    public Parameter parameter() {
        return parameter;
    }

    @Override
    public IExecutableInfo executableInfo() {
        return executableInfo;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public JavaType type() {
        return type;
    }

}
