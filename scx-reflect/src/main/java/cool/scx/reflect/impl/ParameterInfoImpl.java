package cool.scx.reflect.impl;

import cool.scx.reflect.ExecutableInfo;
import cool.scx.reflect.ParameterInfo;
import cool.scx.reflect.TypeInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public final class ParameterInfoImpl implements ParameterInfo {

    private final Parameter rawParameter;
    private final ExecutableInfo declaringExecutable;
    private final String name;
    private final Annotation[] annotations;
    private final TypeInfo parameterType;

    ParameterInfoImpl(Parameter parameter, ExecutableInfo declaringExecutable) {
        this.rawParameter = parameter;
        this.declaringExecutable = declaringExecutable;
        this.name = this.rawParameter.getName();
        this.parameterType = TypeFactory.getType(this.rawParameter.getParameterizedType(), this.declaringExecutable.declaringClass().bindings());
        this.annotations = this.rawParameter.getDeclaredAnnotations();
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
    public TypeInfo parameterType() {
        return parameterType;
    }

    @Override
    public Annotation[] annotations() {
        return annotations;
    }

    @Override
    public String toString() {
        return parameterType.toString() + " " + name();
    }

}
