package cool.scx.bean.x;

public interface BeanContext {

    Object getBean(BeanFactory beanFactory);

    Class<?> beanClass();

}
