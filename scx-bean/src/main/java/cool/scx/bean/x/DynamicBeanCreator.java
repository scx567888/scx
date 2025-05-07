package cool.scx.bean.x;

import java.util.function.Function;

/// 动态生成
public final class DynamicBeanCreator<T> implements BeanCreator {

    private final Function<BeanFactory, T> beanCreator;
    private final Class<T> beanClass;

    public DynamicBeanCreator(Function<BeanFactory, T> beanCreator, Class<T> beanClass) {
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
