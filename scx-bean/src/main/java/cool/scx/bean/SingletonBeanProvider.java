package cool.scx.bean;

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
