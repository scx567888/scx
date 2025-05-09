package cool.scx.bean.provider.injecting;

import cool.scx.reflect.FieldInfo;

/// 依赖上下文
///
/// @param beanClass 正在注入的类
/// @param singleton 是否是单例的
/// @param fieldInfo 正在注入的 Field
record DependentContext(Class<?> beanClass, boolean singleton, FieldInfo fieldInfo) {

}
