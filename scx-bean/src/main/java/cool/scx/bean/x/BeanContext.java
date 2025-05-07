package cool.scx.bean.x;

import java.util.List;

public interface BeanContext {

    boolean singleton();

    Object create(BeanFactory beanFactory);

    Object createAndInject(BeanFactory beanFactory, List<BeanInjector> injectors);

    Class<?> beanClass();

}
