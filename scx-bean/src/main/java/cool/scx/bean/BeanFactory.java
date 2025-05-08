package cool.scx.bean;

import cool.scx.bean.exception.DuplicateBeanNameException;
import cool.scx.bean.exception.NoSuchBeanException;
import cool.scx.bean.exception.NoUniqueBeanException;
import cool.scx.bean.provider.BeanProvider;
import cool.scx.bean.resolver.BeanResolver;

import java.util.List;

/// BeanFactory
public interface BeanFactory {

    /// 根据 名称 获取 Bean
    Object getBean(String name) throws NoSuchBeanException;

    /// 根据 类型 获取 Bean
    <T> T getBean(Class<T> requiredType) throws NoSuchBeanException, NoUniqueBeanException;

    /// 根据 名称和类型 获取 Bean
    <T> T getBean(String name, Class<T> requiredType) throws NoSuchBeanException;

    /// 获取所有 Bean 的名字
    String[] getBeanNames();

    /// 注册一个单例的 Bean
    void registerBean(String name, Object bean) throws DuplicateBeanNameException;

    /// 根据 Class 注册一个 Bean
    void registerBeanClass(String name, Class<?> beanClass, boolean singleton) throws DuplicateBeanNameException;

    /// 注册一个 Bean 提供器
    void registerBeanProvider(String name, BeanProvider beanProvider) throws DuplicateBeanNameException;

    /// 添加一个 Bean 依赖解析器
    void addBeanResolver(BeanResolver beanResolver);

    /// 获取所有 Bean 依赖解析器
    List<BeanResolver> beanResolvers();

    /// 初始化所有 Bean
    void initializeBeans();

    /// 根据 Class 注册一个 Bean, 单例模式
    default void registerBeanClass(String name, Class<?> beanClass) throws DuplicateBeanNameException {
        registerBeanClass(name, beanClass, true);
    }

}
