package cool.scx.bean;

/// bean 创建器
public interface BeanCreator {

    /// 创建 bean
    Object create(BeanFactory beanFactory);

    /// bean 类型
    Class<?> beanClass();

}
