package cool.scx.bean.x;

public interface BeanFactory {

    Object getBean(String name);

    <T> T getBean(Class<T> requiredType);

    void registerBeanCreator(String name, BeanCreator beanCreator);

    /// 初始化所有 bean
    void initializeBeans();

    default void registerBean(String name, Object instance) {
        registerBeanCreator(name, new ExistingBeanCreator(instance));
    }

    default void registerBeanClass(String name, Class<?> beanClass) {
        registerBeanCreator(name, new AnnotationConfigBeanCreator(beanClass));
    }

}
