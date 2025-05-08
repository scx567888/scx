package cool.scx.bean.provider;

import cool.scx.bean.BeanFactory;

/// 根据一个 已经存在的 Bean 创建
public record InstanceBeanProvider(Object bean) implements BeanProvider {

    @Override
    public Object getBean(BeanFactory beanFactory) {
        return bean;
    }

    @Override
    public Class<?> beanClass() {
        return bean.getClass();
    }

}
