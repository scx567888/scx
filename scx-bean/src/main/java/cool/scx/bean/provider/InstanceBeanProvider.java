package cool.scx.bean.provider;

import cool.scx.bean.BeanFactory;

/// 根据一个 已经存在的 Bean 创建
///
/// @author scx567888
/// @version 0.0.1
public record InstanceBeanProvider(Object bean) implements BeanProvider {

    @Override
    public Object getBean(BeanFactory beanFactory) {
        return bean;
    }

    @Override
    public boolean singleton() {
        return true;
    }

    @Override
    public Class<?> beanClass() {
        return bean.getClass();
    }

}
