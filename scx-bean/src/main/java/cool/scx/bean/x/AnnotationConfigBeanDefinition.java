package cool.scx.bean.x;

import cool.scx.reflect.ClassInfoFactory;
import cool.scx.reflect.ConstructorInfo;

import static cool.scx.reflect.AccessModifier.PUBLIC;

/// 根据注解来处理的 bean 定义
public class AnnotationConfigBeanDefinition implements BeanDefinition {

    private final Class<?> beanClass;
    private final boolean singleton;
    private final ConstructorInfo preferredConstructor;

    public AnnotationConfigBeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.singleton = initSingleton(beanClass);
        this.preferredConstructor = initPreferredConstructor(beanClass);
    }

    public static boolean initSingleton(Class<?> beanClass) {
        //todo 这里后期需要从注解来 解析
        return true;
    }

    public static ConstructorInfo initPreferredConstructor(Class<?> beanClass) {
        // todo 这里后期需要根据注解来解析 
        var classInfo = ClassInfoFactory.getClassInfo(beanClass);
        ConstructorInfo coreConstructor = null;
        for (var constructor : classInfo.constructors()) {
            if (constructor.accessModifier() == PUBLIC) {
                coreConstructor = constructor;
            }
        }
        if (coreConstructor == null) {
            throw new IllegalArgumentException("No constructor found for " + beanClass);
        } else {
            return coreConstructor;
        }
    }

    @Override
    public boolean singleton() {
        return singleton;
    }

    @Override
    public Class<?> beanClass() {
        return beanClass;
    }

    @Override
    public ConstructorInfo preferredConstructor() {
        return preferredConstructor;
    }

}
