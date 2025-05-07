package cool.scx.bean.x;

import static cool.scx.bean.x.BeanHelper.injectFieldAndMethod;

public class BeanContextImpl implements BeanContext {

    private final BeanCreator beanCreator;
    private final boolean singleton;
    private Object beanInstance;
    private boolean alreadyInjected;

    public BeanContextImpl(BeanCreator beanCreator, boolean singleton) {
        this.beanCreator = beanCreator;
        this.singleton = singleton;
        this.beanInstance = null;
        this.alreadyInjected = false;
    }

    private Object create(BeanFactory beanFactory) {
        if (singleton) {
            if (beanInstance == null) {
                beanInstance = beanCreator.create(beanFactory);
            }
            return beanInstance;
        } else {
            return beanCreator.create(beanFactory);
        }
    }

    @Override
    public Object getBean(BeanFactory beanFactory) {
        var bean = create(beanFactory);
        // 单例模式
        if (singleton) {
            //已经注入 直接返回
            if (alreadyInjected) {
                return bean;
            }
            alreadyInjected = true;
            //循环注入
            injectFieldAndMethod(bean, beanClass(), beanFactory);
            return bean;
        } else {
            injectFieldAndMethod(bean, beanClass(), beanFactory);
            return bean;
        }
    }

    @Override
    public Class<?> beanClass() {
        return beanCreator.beanClass();
    }

}
