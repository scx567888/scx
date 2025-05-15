package cool.scx.bean.provider;

import cool.scx.bean.BeanFactory;

/// 单例的 Bean 提供器
///
/// @author scx567888
/// @version 0.0.1
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
    public boolean singleton() {
        return true;
    }

    @Override
    public Class<?> beanClass() {
        return beanProvider.beanClass();
    }

}
