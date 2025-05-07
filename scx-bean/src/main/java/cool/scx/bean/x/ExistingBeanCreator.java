package cool.scx.bean.x;

/// 根据一个 已经存在的 bean
public class ExistingBeanCreator implements BeanCreator {

    private final Object bean;

    public ExistingBeanCreator(Object bean) {
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
