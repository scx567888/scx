package cool.scx.bean.x;

import java.util.List;

public interface BeanFactory {

    Object getBean(String name);

    <T> T getBean(Class<T> requiredType);

    void registerBeanContext(String name, BeanContext beanContext);
    
    BeanContext getBeanContext(String name);
    
    BeanContext getBeanContext(Class<?> requiredType);

    void addBeanInjector(BeanInjector beanInjector);
    
    List<BeanInjector> beanInjectors();

    void addBeanProcessor(BeanProcessor beanProcessor);

    List<BeanProcessor> beanProcessors();

    /// 初始化所有 bean
    void initializeBeans();

    String[] getBeanNames();

    default void registerBean(String name, Object instance) {
        registerBeanContext(name, new InstanceBeanContext(instance));
    }

    default void registerBeanClass(String name, Class<?> beanClass) {
        //这里是否单例需要 读取 beanClass 
        registerBeanContext(name, new BeanContextImpl(new AnnotationConfigBeanCreator(beanClass), true));
    }

    default void registerBeanCreator(String name, BeanCreator beanCreator, boolean singleton) {
        registerBeanContext(name, new BeanContextImpl(beanCreator, singleton));
    }

}
