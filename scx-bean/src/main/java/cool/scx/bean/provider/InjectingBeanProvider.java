package cool.scx.bean.provider;

import cool.scx.bean.BeanFactory;

import static cool.scx.bean.Helper.injectFieldAndMethod;

/// 支持字段和方法注入 的 提供器
public class InjectingBeanProvider implements BeanProvider {

    private final BeanProvider beanProvider;
    private final boolean singleton;
    private boolean alreadyInjected;

    public InjectingBeanProvider(BeanProvider beanProvider, boolean singleton) {
        this.beanProvider = beanProvider;
        this.singleton = singleton;
        this.alreadyInjected = false;
    }

    @Override
    public Object getBean(BeanFactory beanFactory) {
        var bean = beanProvider.getBean(beanFactory);
        // 单例模式
        if (singleton) {
            //已经注入 直接返回
            if (alreadyInjected) {
                return bean;
            }
            alreadyInjected = true;
        }
        injectFieldAndMethod(bean, beanClass(), beanFactory);
        return bean;
    }

    @Override
    public Class<?> beanClass() {
        return beanProvider.beanClass();
    }

}
