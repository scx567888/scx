package cool.scx.bean.x;

/// Bean 上下文
public interface BeanContext {

    Object getBean(BeanFactory beanFactory);

    Class<?> beanClass();

}
