package cool.scx.bean.provider.annotation_config;

import cool.scx.reflect.ConstructorInfo;
import cool.scx.reflect.ParameterInfo;

/// 依赖上下文
///
/// @param beanClass   正在创建的类
/// @param constructor 使用的构造函数
/// @param parameter   正在创建的参数
record DependentContext(Class<?> beanClass, ConstructorInfo constructor, ParameterInfo parameter) {

}
