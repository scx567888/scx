package cool.scx.bean.provider;

import cool.scx.bean.BeanFactory;

/// Bean 提供器
///
/// @author scx567888
/// @version 0.0.1
public interface BeanProvider {

    /// 获取 Bean
    Object getBean(BeanFactory beanFactory);

    /// 表示 getBean() 是否始终返回同一实例
    boolean singleton();

    /// 表示 getBean() 返回的对象类型
    Class<?> beanClass();

}
