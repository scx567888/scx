package cool.scx.bean.x;

import java.util.function.Function;

/// 动态生成
public class DynamicBeanCreator implements BeanCreator {

    private final Function<BeanFactory, ?> beanCreator;
    private final Class<?> beanClass;

    public <T> DynamicBeanCreator(Function<BeanFactory, T> beanCreator, Class<T> beanClass) {
        this.beanCreator = beanCreator;
        this.beanClass = beanClass;
    }

    @Override
    public Object create(BeanFactory beanFactory) {
        return beanCreator.apply(beanFactory);
    }

    @Override
    public Class<?> beanClass() {
        return beanClass;
    }

}
