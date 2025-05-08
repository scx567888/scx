package cool.scx.bean.provider;

import cool.scx.bean.BeanFactory;
import cool.scx.reflect.ClassInfoFactory;
import cool.scx.reflect.ConstructorInfo;

import java.lang.reflect.InvocationTargetException;

import static cool.scx.reflect.AccessModifier.PUBLIC;

/// 根据 class 创建 Bean
public class AnnotationConfigBeanProvider implements BeanProvider {

    private final Class<?> beanClass;
    private final ConstructorInfo constructor;

    public AnnotationConfigBeanProvider(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.constructor = findPreferredConstructor(beanClass);
    }

    public static ConstructorInfo findPreferredConstructor(Class<?> beanClass) {
        // todo 暂时使用 找到的第一个 public 构造函数 后期可能需要 注解
        var classInfo = ClassInfoFactory.getClassInfo(beanClass);
        for (var constructor : classInfo.constructors()) {
            if (constructor.accessModifier() == PUBLIC) {
                return constructor;
            }
        }
        throw new IllegalArgumentException("No constructor found for " + beanClass);
    }

    public Object newInstance(BeanFactory beanFactory) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        //1, 获取参数
        var parameters = constructor.parameters();
        //2, 创建参数数组
        var objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            var parameter = parameters[i];
            //3, 尝试从 beanResolver 中获取 
            for (var beanResolver : beanFactory.beanResolvers()) {
                var value = beanResolver.resolveConstructorArgument(parameter);
                if (value != null) {
                    objects[i] = value;
                    break;
                }
            }
        }
        return constructor.newInstance(objects);
    }

    @Override
    public Class<?> beanClass() {
        return beanClass;
    }

    @Override
    public Object getBean(BeanFactory beanFactory) {
        // 创建实例
        try {
            return newInstance(beanFactory);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean singleton() {
        return false;
    }

}
