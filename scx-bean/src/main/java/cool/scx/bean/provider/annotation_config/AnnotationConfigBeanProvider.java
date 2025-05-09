package cool.scx.bean.provider.annotation_config;

import cool.scx.bean.BeanFactory;
import cool.scx.bean.exception.BeanCreationException;
import cool.scx.bean.provider.BeanProvider;
import cool.scx.reflect.ConstructorInfo;

import java.lang.reflect.InvocationTargetException;

import static cool.scx.bean.provider.annotation_config.CircularDependencyChecker.endDependencyCheck;
import static cool.scx.bean.provider.annotation_config.CircularDependencyChecker.startDependencyCheck;
import static cool.scx.bean.provider.annotation_config.Helper.findPreferredConstructor;
import static cool.scx.bean.provider.annotation_config.Helper.resolveConstructorArgument;

/// 根据 class 创建 Bean
public class AnnotationConfigBeanProvider implements BeanProvider {

    private final Class<?> beanClass;
    private final ConstructorInfo constructor;

    public AnnotationConfigBeanProvider(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.constructor = findPreferredConstructor(beanClass);
    }

    @Override
    public Class<?> beanClass() {
        return beanClass;
    }

    @Override
    public Object getBean(BeanFactory beanFactory) {

        var parameters = constructor.parameters();
        var objects = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            var parameter = parameters[i];
            // 开始循环依赖检查
            startDependencyCheck(new DependentContext(this.beanClass, this.constructor, parameter));
            try {
                objects[i] = resolveConstructorArgument(beanFactory, parameter);
            } catch (Exception e) {
                throw new BeanCreationException("在类 " + this.beanClass.getName() + "中, 解析构造参数 " + parameter.name() + " 时发生异常 ", e);
            } finally {
                //结束检查
                endDependencyCheck();
            }
        }

        try {
            return constructor.newInstance(objects);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanCreationException("在类 " + this.beanClass.getName() + "中, 创建 bean 时发生异常 ", e);
        } catch (InvocationTargetException e) {
            throw new BeanCreationException("在类 " + this.beanClass.getName() + "中, 创建 bean 时发生异常 ", e.getCause());
        }
    }

    @Override
    public boolean singleton() {
        return false;
    }

}
