package cool.scx.bean.x;

import cool.scx.reflect.ConstructorInfo;

public interface BeanDefinition {

    /// 是否单例模式
    boolean singleton();

    /// bean 类型
    Class<?> beanClass();

    /// 首选构造函数
    ConstructorInfo preferredConstructor();

}
