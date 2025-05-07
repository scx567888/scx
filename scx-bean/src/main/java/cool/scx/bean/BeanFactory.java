package cool.scx.bean;

import java.util.List;

public interface BeanFactory {

    /// 根据名称 获取 Bean
    Object getBean(String name);

    /// 根据 类型 获取 Bean
    <T> T getBean(Class<T> requiredType);

    /// 注册一个单例的 Bean
    void registerBean(String name, Object instance);

    /// 根据 Class 注册一个 Bean 
    void registerBeanClass(String name, Class<?> beanClass);

    /// 注册一个 Bean 上下文
    void registerBeanContext(String name, BeanContext beanContext);

    /// 添加一个 Bean 依赖解析器
    void addBeanDependencyResolver(BeanDependencyResolver beanDependencyResolver);

    /// 获取所有 Bean 依赖解析器
    List<BeanDependencyResolver> beanDependencyResolvers();

    /// 初始化所有 Bean
    void initializeBeans();

    /// 获取所有 Bean 的名字
    String[] getBeanNames();

}
