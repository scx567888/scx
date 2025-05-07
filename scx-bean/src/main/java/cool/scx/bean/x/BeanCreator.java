package cool.scx.bean.x;

public interface BeanCreator {
    
    Object create();

    Class<?> beanClass();
    
}
