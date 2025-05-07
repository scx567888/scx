package cool.scx.bean.x;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanInjector implements BeanInjector {

    private final BeanFactory beanFactory;

    public AutowiredAnnotationBeanInjector(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void inject(Object bean) {
        //todo 这里需要注入字段
        Class<?> beanClass = bean.getClass();
        for (Field field : beanClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(Autowired.class) != null) {
                Class<?> type = field.getType();
                var o = beanFactory.getBean(type);
                try {
                    field.set(bean, o);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
