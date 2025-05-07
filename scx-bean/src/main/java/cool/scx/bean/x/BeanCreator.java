package cool.scx.bean.x;

public interface BeanCreator {
    
    /// 创建 bean
    Object create();

    /// bean 类型
    Class<?> beanClass();
    
}
