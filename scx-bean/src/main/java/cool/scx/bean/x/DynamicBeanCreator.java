package cool.scx.bean.x;

import java.util.function.Supplier;

/// 动态生成 
public class DynamicBeanCreator implements BeanCreator {

    private final Supplier<?> supplier;
    private final Class<?> beanClass;

    public <T> DynamicBeanCreator(Supplier<T> supplier, Class<T> beanClass) {
        this.supplier = supplier;
        this.beanClass = beanClass;
    }

    @Override
    public Object create() {
        return supplier.get();
    }

    @Override
    public Class<?> beanClass() {
        return beanClass;
    }

}
