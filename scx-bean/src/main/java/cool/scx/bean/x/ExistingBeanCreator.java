package cool.scx.bean.x;

/// 根据一个 已经存在的 bean
public record ExistingBeanCreator(Object bean) implements BeanCreator {

    @Override
    public Object create(BeanFactory beanFactory) {
        return bean;
    }

    @Override
    public Class<?> beanClass() {
        return bean.getClass();
    }

}
