package cool.scx.bean.provider;

import cool.scx.bean.BeanFactory;

/// Bean 提供器
public interface BeanProvider {

    /// 获取 Bean
    Object getBean(BeanFactory beanFactory);

    /// Bean 类型
    Class<?> beanClass();

}
