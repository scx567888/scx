package cool.scx.bean.x;

import cool.scx.bean.x.annotation.Value;

import java.lang.reflect.Field;
import java.util.Map;

public class ValueAnnotationBeanInjector implements BeanInjector {

    private final Map<String, String> map;

    public ValueAnnotationBeanInjector(Map<String,String> map) {
        this.map = map;
    }

    @Override
    public void inject(Object bean) {
        // todo 此处待处理
        var beanClass = bean.getClass();
        for (Field field : beanClass.getDeclaredFields()) {
            field.setAccessible(true);
            Value annotation = field.getAnnotation(Value.class);
            if (annotation != null) {
                String value = annotation.value();
                String s = map.get(value);
                try {
                    field.set(bean, s);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
