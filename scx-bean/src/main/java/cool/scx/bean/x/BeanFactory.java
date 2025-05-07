package cool.scx.bean.x;

public interface BeanFactory {

    Object getBean(String name);

    <T> T getBean(Class<T> requiredType);

    void registerBean(String name, Object instance);

    void registerBeanClass(String name, Class<?> beanClass);

    void registerBeanDefinition(String name, BeanDefinition beanDefinition);

    /// 注册完 bean 的时候需要调用一次
    void refresh();

}
