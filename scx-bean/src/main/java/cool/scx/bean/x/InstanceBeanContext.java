package cool.scx.bean.x;

import java.util.List;

/// 根据一个 已经存在的 bean 创建而来
public final class InstanceBeanContext implements BeanContext {

    private final Object bean;
    private boolean alreadyInjected;

    public InstanceBeanContext(Object bean) {
        this.bean = bean;
        this.alreadyInjected = false;
    }

    @Override
    public Object create(BeanFactory beanFactory) {
        return bean;
    }

    @Override
    public Object createAndInject(BeanFactory beanFactory, List<BeanInjector> injectors) {
        if (alreadyInjected) {
            return bean;
        }
        alreadyInjected = true;
        for (var injector : injectors) {
            injector.inject(bean);
        }
        return bean;
    }

    @Override
    public Class<?> beanClass() {
        return bean.getClass();
    }

    @Override
    public boolean singleton() {
        return true;
    }

}
