package cool.scx.bean;

/// Bean 上下文
public interface BeanContext {

    Object getBean(BeanFactory beanFactory);

    Class<?> beanClass();

}
