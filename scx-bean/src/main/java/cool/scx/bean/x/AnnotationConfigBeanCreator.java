package cool.scx.bean.x;

import cool.scx.reflect.ClassInfoFactory;
import cool.scx.reflect.ConstructorInfo;

import java.lang.reflect.InvocationTargetException;

import static cool.scx.reflect.AccessModifier.PUBLIC;

/// 根据注解来处理的 bean 定义
public class AnnotationConfigBeanCreator implements BeanCreator {

    private final Class<?> beanClass;
    private final boolean singleton;
    private final ConstructorInfo constructor;
    private Object beanInstance;

    public AnnotationConfigBeanCreator(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.singleton = initSingleton(beanClass);
        this.constructor = initPreferredConstructor(beanClass);
    }

    public static boolean initSingleton(Class<?> beanClass) {
        //todo 这里后期需要从注解来 解析 暂时先为 true
        return true;
    }

    public static ConstructorInfo initPreferredConstructor(Class<?> beanClass) {
        // todo 这里后期需要根据注解来解析 暂时先使用 第一个 public
        var classInfo = ClassInfoFactory.getClassInfo(beanClass);
        ConstructorInfo coreConstructor = null;
        for (var constructor : classInfo.constructors()) {
            if (constructor.accessModifier() == PUBLIC) {
                coreConstructor = constructor;
                break;
            }
        }
        if (coreConstructor == null) {
            throw new IllegalArgumentException("No constructor found for " + beanClass);
        } else {
            return coreConstructor;
        }
    }

    @Override
    public Class<?> beanClass() {
        return beanClass;
    }

    public Object create0(BeanFactory beanFactory) {
        var parameters = constructor.parameters();
        var objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            var parameter = parameters[i];
            objects[i] = beanFactory.getBean(parameter.parameter().getType()); //todo 获取 构造函数 
        }
        try {
            return constructor.newInstance(objects);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object create(BeanFactory beanFactory) {
        if (singleton) {
            if (beanInstance == null) {
                beanInstance = create0(beanFactory);
            }
            return beanInstance;
        } else {
            return create0(beanFactory);
        }
    }

}
