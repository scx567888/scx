package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.reflect.Parameter;

/// 仅做规范
public interface IParameterInfo {

    Parameter parameter();

    IExecutableInfo executableInfo();

    String name();

    JavaType type();

}
