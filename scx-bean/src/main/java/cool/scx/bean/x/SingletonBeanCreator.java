package cool.scx.bean.x;

/// 单例 bean 创建器
public class SingletonBeanCreator implements BeanCreator {

    private final Object bean;

    public SingletonBeanCreator(Object bean) {
        this.bean = bean;
    }

    @Override
    public Object create() {
        return bean;
    }

    @Override
    public Class<?> beanClass() {
        return bean.getClass();
    }

}
