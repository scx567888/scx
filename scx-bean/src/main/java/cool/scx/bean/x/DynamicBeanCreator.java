package cool.scx.bean.x;

import java.util.function.Function;

/// 动态生成
public record DynamicBeanCreator<T>(Function<BeanFactory, T> beanCreator, Class<T> beanClass) implements BeanCreator {

    @Override
    public Object create(BeanFactory beanFactory) {
        return beanCreator.apply(beanFactory);
    }

}
