package cool.scx.bean.provider;

import cool.scx.bean.BeanFactory;
import cool.scx.reflect.AccessModifier;
import cool.scx.reflect.ClassInfoFactory;

/// 支持字段和方法注入 的 提供器
public class InjectingBeanProvider implements BeanProvider {

    private final BeanProvider beanProvider;
    private boolean alreadyInjected;

    public InjectingBeanProvider(BeanProvider beanProvider) {
        this.beanProvider = beanProvider;
        this.alreadyInjected = false;
    }

    @Override
    public Object getBean(BeanFactory beanFactory) {
        var bean = beanProvider.getBean(beanFactory);
        // 单例模式
        if (beanProvider.singleton()) {
            //已经注入 直接返回
            if (alreadyInjected) {
                return bean;
            }
            alreadyInjected = true;
        }
        injectFieldAndMethod(bean, beanFactory);
        return bean;
    }

    @Override
    public boolean singleton() {
        return beanProvider.singleton();
    }

    @Override
    public Class<?> beanClass() {
        return beanProvider.beanClass();
    }

    private void injectFieldAndMethod(Object bean, BeanFactory beanFactory) {
        var classInfo = ClassInfoFactory.getClassInfo(beanClass());
        var fieldInfos = classInfo.allFields();
        for (var fieldInfo : fieldInfos) {
            //只处理 public 字段
            if (fieldInfo.accessModifier() == AccessModifier.PUBLIC) {
                fieldInfo.setAccessible(true);
                for (var resolver : beanFactory.beanResolvers()) {
                    var fieldValue = resolver.resolveFieldValue(fieldInfo);
                    if (fieldValue != null) {
                        try {
                            fieldInfo.set(bean, fieldValue);
                            break;
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("注入 Field 异常 !!!", e);
                        }
                    }
                }
            }
        }
        var methodInfos = classInfo.allMethods();
        for (var methodInfo : methodInfos) {
            //只处理 public 方法
            if (methodInfo.accessModifier() == AccessModifier.PUBLIC) {
                methodInfo.setAccessible(true);
                for (var resolver : beanFactory.beanResolvers()) {
                    var b = resolver.resolveMethod(methodInfo);
                    if (b) {
                        break;
                    }
                }
            }
        }
    }

}
