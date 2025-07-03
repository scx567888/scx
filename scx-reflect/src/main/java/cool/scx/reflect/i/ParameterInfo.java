package cool.scx.reflect.i;

import java.lang.reflect.Parameter;

public interface ParameterInfo extends AnnotatedElementInfo {
    
    /// 原始 Parameter
    Parameter rawParameter();
    
    /// 名称
    String name();
    
    /// 持有当前参数的执行器
    ExecutableInfo declaringExecutable();
    
    /// 参数本身的类型
    ClassInfo parameterType();
    
}
