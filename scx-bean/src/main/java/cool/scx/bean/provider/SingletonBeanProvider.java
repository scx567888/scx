package cool.scx.bean.provider;

import cool.scx.bean.BeanFactory;

/// 单例的 Bean 提供器
public class SingletonBeanProvider implements BeanProvider {

    private final BeanProvider beanProvider;
    private Object beanInstance;

    public SingletonBeanProvider(BeanProvider beanProvider) {
        this.beanProvider = beanProvider;
        this.beanInstance = null;
    }

    @Override
    public Object getBean(BeanFactory beanFactory) {
        if (beanInstance == null) {
            beanInstance = beanProvider.getBean(beanFactory);
        }
        return beanInstance;
    }

    @Override
    public Class<?> beanClass() {
        return beanProvider.beanClass();
    }

}
