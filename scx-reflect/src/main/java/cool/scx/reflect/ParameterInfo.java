package cool.scx.reflect;

import java.lang.reflect.Parameter;

/// ParameterInfo
///
/// @author scx567888
/// @version 0.0.1
public interface ParameterInfo extends AnnotatedElementInfo {

    /// 原始 Parameter
    Parameter rawParameter();

    /// 持有当前参数的执行器
    ExecutableInfo declaringExecutable();

    /// 名称
    String name();

    /// 参数本身的类型
    TypeInfo parameterType();

}
