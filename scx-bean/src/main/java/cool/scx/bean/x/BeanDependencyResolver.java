package cool.scx.bean.x;

import cool.scx.reflect.FieldInfo;
import cool.scx.reflect.MethodInfo;
import cool.scx.reflect.ParameterInfo;

/// 提供配置一个 bean 所需的依赖
public interface BeanDependencyResolver {

    /// 提供 构造函数 参数 无法处理返回 null
    Object resolveConstructorArgument(ParameterInfo parameterInfo);
    
    /// 提供 字段 无法处理返回 null
    Object resolveFieldValue(FieldInfo fieldInfo);
    
    /// 配置 方法 无法配置 返回 false 
    boolean resolveMethod(MethodInfo methodInfo);

}
