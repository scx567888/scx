package cool.scx.reflect.i;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public final class ParameterInfoImpl implements ParameterInfo {

    private final Parameter rawParameter;
    private final ExecutableInfo declaringExecutable;
    private final String name;
    private final Annotation[] annotations;
    private final ClassInfo parameterType;

    public ParameterInfoImpl(Parameter parameter, ExecutableInfo declaringExecutable) {
        this.rawParameter = parameter;
        this.declaringExecutable = declaringExecutable;
        this.name = parameter.getName();
        this.parameterType = ScxReflect.getClassInfo(parameter.getParameterizedType());
        this.annotations = parameter.getDeclaredAnnotations();
    }

    @Override
    public Parameter rawParameter() {
        return rawParameter;
    }

    @Override
    public ExecutableInfo declaringExecutable() {
        return declaringExecutable;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ClassInfo parameterType() {
        return parameterType;
    }

    @Override
    public Annotation[] annotations() {
        return annotations;
    }

}
